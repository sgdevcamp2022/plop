package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName


data class PostSignUpResponse(
    @SerializedName("data")
    val signUpDto: SignUpDto,
    val message: String,
    val result: String
)