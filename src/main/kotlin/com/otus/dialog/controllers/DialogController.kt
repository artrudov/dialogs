package com.otus.dialog.controllers

import com.otus.dialog.domain.MessageResponse

import com.otus.dialog.domain.NewMessage
import com.otus.dialog.services.DialogService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/dialogs")
class DialogController {
  private val logger = LoggerFactory.getLogger(DialogController::class.java)

  @Autowired
  lateinit var dialogService: DialogService

  @PostMapping("{recipientId}")
  fun add(
    @RequestBody dialog: NewMessage,
    @RequestHeader("X-User-Id") senderId: Long,
    @PathVariable recipientId: Long
  ): ResponseEntity<String> {
    logger.info("Adding message: {}, between users {} and {}", dialog, senderId, recipientId)

    dialogService.save(dialog, recipientId, senderId)

    return ResponseEntity
      .status(HttpStatus.CREATED)
      .body("New dialog has been saved")
  }

  @GetMapping("{recipientId}")
  fun findAllMessages(
    @RequestHeader("X-User-Id") senderId: Long,
    @PathVariable recipientId: Long,
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