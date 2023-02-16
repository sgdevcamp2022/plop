package com.plop.plopmessenger.data.dto.request.chat


import com.google.gson.annotations.SerializedName

data class PostMessageRequest(
    val content: String,
    @SerializedName("message_type")
    val messageType: String,
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("sender_id")
    val senderId: String
)