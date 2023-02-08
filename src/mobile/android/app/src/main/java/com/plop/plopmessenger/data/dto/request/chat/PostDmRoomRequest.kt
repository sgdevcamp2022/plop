package com.plop.plopmessenger.data.dto.request.chat


import com.google.gson.annotations.SerializedName

data class PostDmRoomRequest(
    @SerializedName("message_to")
    val messageTo: String
)