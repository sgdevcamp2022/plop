package com.plop.plopmessenger.data.dto.request.chat


import com.google.gson.annotations.SerializedName

data class PostInvitationRequest(
    val members: List<String>,
    @SerializedName("room_id")
    val roomId: String
)