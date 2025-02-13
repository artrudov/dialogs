package com.otus.dialog.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class NewMessage(val text: String)

data class Message(
  @JsonProperty("userFrom") var userFrom: Long,
  @JsonProperty("userTo") var userTo: Long,
  @JsonProperty("author") val author: Long,
  @JsonProperty("text") val text: String
)

data class MessageResponse(
  @JsonProperty("author") val author: Long,
  @JsonProperty("text") val text: String
)