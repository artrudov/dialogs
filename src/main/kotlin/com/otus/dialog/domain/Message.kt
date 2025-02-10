package com.otus.dialog.domain

data class NewMessage(val text: String)

data class Message(
  val id: String?,
  val userFrom: Long,
  val userTo: Long,
  val text: String
)