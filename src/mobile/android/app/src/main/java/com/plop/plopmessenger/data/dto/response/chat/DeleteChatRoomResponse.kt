package com.plop.plopmessenger.data.dto.response.chat


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.request.ChatRoomId

data class DeleteChatRoomResponse(
    @SerializedName("data")
    val deleteChatRoomDto: ChatRoomId,
    val message: String
)