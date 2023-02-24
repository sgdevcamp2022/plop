package com.plop.plopmessenger.data.dto.request.user


import com.google.gson.annotations.SerializedName

data class PostPasswordNewRequest(
    val email: String,
    val newPassword: String,
    val userId: String
)