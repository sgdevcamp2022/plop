package com.plop.plopmessenger.data.dto.response.user


import com.google.gson.annotations.SerializedName

data class PostEmailVerifyResponse(
    val email: String,
    val userId: String,
    val verificationCode: String
)