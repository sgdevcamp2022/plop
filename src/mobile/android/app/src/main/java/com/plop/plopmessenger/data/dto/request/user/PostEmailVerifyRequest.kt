package com.plop.plopmessenger.data.dto.request.user


import com.google.gson.annotations.SerializedName

data class PostEmailVerifyRequest(
    val email: String,
    val userId: String,
    val verificationCode: String
)