package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName


data class PostPasswordNewResponse(
    @SerializedName("data")
    val postPasswordNewResponse: PasswordNewDto,
    val message: String,
    val result: String
)