package com.plop.plopmessenger.data.dto.request.chat

import com.plop.plopmessenger.data.dto.request.ChatRoomId


data class DeleteChatRoomRequest(
    val chatRoomId: ChatRoomId,
    val message: String
)