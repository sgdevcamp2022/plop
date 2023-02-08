package com.plop.plopmessenger.data.dto.request.user


import com.google.gson.annotations.SerializedName

data class PostSignUpRequest(
    val email: String,
    val nickname: String,
    val password: String,
    val userId: String
)