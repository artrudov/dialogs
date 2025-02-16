package com.otus.dialog.repositories

import com.fasterxml.jackson.databind.ObjectMapper
import com.otus.dialog.domain.Message
import com.otus.dialog.domain.MessageResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import redis.clients.jedis.JedisPool
import java.security.MessageDigest


@Slf4j
@Repository
class DialogRepository {
  @Autowired
  lateinit var jedisPool: JedisPool

  // Загрузка скрипта из файла
  private val sendMessageScript: String by lazy {
    ClassPathResource("scripts/sendMessagesScript.lua")
      .inputStream.bufferedReader().use { it.readText() }
  }

  // Получение SHA-хэша скрипта
  private val sendScriptSha: String by lazy {
    jedisPool.resource.use { jedis ->
      jedis.scriptLoad(sendMessageScript)
    }
  }

  // Загрузка скрипта из файла
  private val readMessageScript: String by lazy {
    ClassPathResource("scripts/readMessagesScript.lua")
      .inputStream.bufferedReader().use { it.readText() }
  }

  // Получение SHA-хэша скрипта
  private val readScriptSha: String by lazy {
    jedisPool.resource.use { jedis ->
      jedis.scriptLoad(readMessageScript)
    }
  }

  @OptIn(ExperimentalStdlibApi::class)
  fun toHash(senderId: Long, recipientId: Long): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest("${senderId}:${recipientId}".toByteArray())

    return digest.toHexString()
  }

  fun save(newMessage: Message) {
    jedisPool.resource.use { resource ->
      resource.evalsha(
        sendScriptSha,
        mutableListOf(toHash(newMessage.userFrom, newMessage.userTo)),
        mutableListOf(
          newMessage.author.toString(),
          newMessage.text
        )
      )
    }
  }

  fun findAll(recipientId: Long, senderId: Long, pageable: Pageable): List<MessageResponse> {
    val result: Any
    val startIndex = (pageable.pageNumber - 1) * pageable.pageSize
    val endIndex = startIndex + pageable.pageSize

    jedisPool.resource.use { resource ->
      result = resource.evalsha(
        readScriptSha,
        mutableListOf(toHash(senderId, recipientId)),
        mutableListOf(
          startIndex.toString(),
          endIndex.toString()
        ),
      )
    }

    val messages: List<MessageResponse> = (result as List<String>).stream()
      .map { row: String ->
        ObjectMapper().readValue(row, MessageResponse::class.java)
      }
      .toList()

    return messages
  }
}