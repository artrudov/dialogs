package com.otus.dialog.controllers

import com.otus.dialog.domain.Message
import com.otus.dialog.domain.MessageResponse

import com.otus.dialog.domain.NewMessage
import com.otus.dialog.services.DialogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dialogs")
class DialogController {
  @Autowired
  lateinit var dialogService: DialogService

  @PostMapping("{recipientId}")
  fun add(
    @RequestBody dialog: NewMessage,
    @RequestHeader("X-User-Id") senderId: Long,
    @PathVariable recipientId: Long
  ): ResponseEntity<String> {
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
    return ResponseEntity
      .ok()
      .body(
        dialogService.findAll(recipientId, senderId, pageable)
      )
  }
}