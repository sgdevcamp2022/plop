package com.plop.plopmessenger.data.dto.response.chat

import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.ChatRoomAndMember


data class PostInvitationResponse(
    @SerializedName("data")
    val postInvitationDto: ChatRoomAndMember,
    val message: String
)