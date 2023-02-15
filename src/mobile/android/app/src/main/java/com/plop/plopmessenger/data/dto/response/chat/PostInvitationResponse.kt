package com.plop.plopmessenger.data.dto.response.chat

import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.ChatRoomAndMember


data class PostInvitationResponse(
    val members: List<String>,
    @SerializedName("room_id")
    val roomId: String
)