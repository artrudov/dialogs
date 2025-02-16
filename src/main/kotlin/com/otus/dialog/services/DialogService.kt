package com.otus.dialog.services

import com.otus.dialog.domain.Message
import com.otus.dialog.domain.MessageResponse
import com.otus.dialog.domain.NewMessage
import com.otus.dialog.repositories.DialogRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DialogService {
  @Autowired
  lateinit var dialogRepository: DialogRepository

  fun save(message: Message) {
    val recipientId = message.userTo
    val senderId = message.userFrom

    if (recipientId == senderId) throw ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      "You can't get messages to yourself"
    )

    dialogRepository.save(
      Message(
        userFrom = if (senderId > recipientId) senderId else recipientId,
        userTo = if (recipientId > senderId) recipientId else senderId,
        author = senderId,
        text = message.text,
      )
    )
  }

  fun save(dialog: NewMessage, recipientId: Long, senderId: Long) {
    if (recipientId == senderId) throw ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      "You can't get messages to yourself"
    )

    dialogRepository.save(
      Message(
        userFrom = if (senderId > recipientId) senderId else recipientId,
        userTo = if (recipientId > senderId) recipientId else senderId,
        author = senderId,
        text = dialog.text,
      )
    )
  }

  fun findAll(recipientId: Long, senderId: Long, pageable: Pageable): List<MessageResponse> {
    if (recipientId == senderId) throw ResponseStatusException(
      HttpStatus.BAD_REQUEST,
      "You can't get messages to yourself"
    )

    return dialogRepository.findAll(
      if (recipientId > senderId) recipientId else senderId,
      if (senderId > recipientId) senderId else recipientId,
      pageable
    )
  }
}