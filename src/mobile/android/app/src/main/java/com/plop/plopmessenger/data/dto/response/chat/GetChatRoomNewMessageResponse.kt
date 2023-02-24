package com.plop.plopmessenger.data.dto.response.chat

import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.Message


data class GetChatRoomNewMessageResponse(
    @SerializedName("data")
    val getChatRoomNewMessageDto: List<Message>,
    val message: String
)