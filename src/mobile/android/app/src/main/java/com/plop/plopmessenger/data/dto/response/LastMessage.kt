package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LastMessage(
    val content: String,
    @SerializedName("created_at")
    val createdAt: LocalDateTime,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("sender_id")
    val senderId: String
)