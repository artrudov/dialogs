package com.otus.dialog.controllers

import com.otus.dialog.domain.Message
import com.otus.dialog.domain.MessageResponse

import com.otus.dialog.services.DialogService
import org.jetbrains.annotations.NotNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/dialogs")
class DialogControllerV2 {
  private val logger = LoggerFactory.getLogger(DialogControllerV2::class.java)

  @Autowired
  lateinit var dialogService: DialogService

  @PostMapping
  fun add(
    @RequestBody message: Message,
  ): ResponseEntity<String> {
    logger.info("Adding message: {}, between users {} and {}", message.text, message.userFrom, message.userTo)

    dialogService.save(message)

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("New dialog has been saved")
  }

  @GetMapping("{recipientId}")
  fun findAllMessages(
    @PathVariable recipientId: Long,
    @NotNull @RequestParam(name = "userFrom") senderId: Long,
    @PageableDefault(size = 10, page = 1) pageable: Pageable
  ): ResponseEntity<List<MessageResponse>> {
    logger.info("Find messages between users {} and {}", senderId, recipientId)

    return ResponseEntity
      .ok()
      .body(
        dialogService.findAll(recipientId, senderId, pageable)
      )
  }
}