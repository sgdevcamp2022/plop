package com.plop.plopmessenger.data.dto.response.fcm


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.User

data class FcmResponse(
    @SerializedName("data")
    val user: User,
    val message: String,
    val result: String
)