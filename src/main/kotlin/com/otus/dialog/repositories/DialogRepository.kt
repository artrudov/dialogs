package com.otus.dialog.repositories

import com.otus.dialog.domain.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.security.MessageDigest

@Repository
class DialogRepository {
  @Autowired
  lateinit var jdbcClient: JdbcClient

  @OptIn(ExperimentalStdlibApi::class)
  fun toHash (senderId: Long, recipientId: Long): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest("${senderId}:${recipientId}".toByteArray())

    return digest.toHexString()
  }

  fun save(dialog: Message) {
    val sql = """
      insert into messages (user_from, user_to, text, sha_code) values (:from, :to, :text, :shaCode);
    """.trimIndent()

    jdbcClient
      .sql(sql)
      .params(mapOf(
        "from" to dialog.userFrom,
        "to" to dialog.userTo,
        "text" to dialog.text,
        "shaCode" to toHash(dialog.userFrom, dialog.userTo)
      ))
      .update()
  }

  fun findAll(recipientId: Long, senderId: Long): List<Message> {
    val sql = """
      select * from messages 
      where sha_code = :shaCode
      order by sent_at desc
    """.trimIndent()

    return jdbcClient
      .sql(sql)
      .params(mapOf(
        "shaCode" to toHash(senderId, recipientId),
      ))
      .query(Message::class.java)
      .list()
  }
}