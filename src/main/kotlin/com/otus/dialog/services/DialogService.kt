package com.otus.dialog.services

import com.otus.dialog.domain.Message
import com.otus.dialog.domain.NewMessage
import com.otus.dialog.repositories.DialogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DialogService {
  @Autowired
  lateinit var dialogRepository: DialogRepository

  fun save(dialog: NewMessage, recipientId: Long, senderId: Long) {
    if (recipientId != senderId)
      dialogRepository.save(
        Message(
          id = null,
          userFrom = senderId,
          userTo = recipientId,
          text = dialog.text,
        )
      )
      else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't send message to yourself")
  }

  fun findAll(recipientId: Long, senderId: Long): List<Message> {
    if (recipientId == senderId) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't get messages to yourself")

    return dialogRepository.findAll(recipientId, senderId)
  }
}