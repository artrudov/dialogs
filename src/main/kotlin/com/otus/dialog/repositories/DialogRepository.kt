package com.otus.dialog.repositories

import com.otus.dialog.domain.Message
import jakarta.annotation.Resource
import org.springframework.data.redis.core.ListOperations
import org.springframework.stereotype.Repository
import java.security.MessageDigest


@Repository
class DialogRepository {
  @Resource(name = "redisTemplate")
  lateinit var listOps: ListOperations<String, Message>

  @OptIn(ExperimentalStdlibApi::class)
  fun toHash(senderId: Long, recipientId: Long): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest("${senderId}:${recipientId}".toByteArray())

    return digest.toHexString()
  }

  fun save(message: Message) {
    listOps.leftPush(
      toHash(message.userFrom, message.userTo),
      message
    )
  }

  fun findAll(recipientId: Long, senderId: Long): List<Message> {
    return listOps.range(toHash(senderId, recipientId), 0, Long.MAX_VALUE)?.toList() ?: listOf()
  }
}