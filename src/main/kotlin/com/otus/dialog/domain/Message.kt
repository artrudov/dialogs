package com.otus.dialog.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class NewMessage(val text: String)

data class Message(
  @JsonProperty("userFrom") val userFrom: Long,
  @JsonProperty("userTo") val userTo: Long,
  @JsonProperty("text") val text: String
)