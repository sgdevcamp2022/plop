package com.plop.plopmessenger.data.dto.response.user


import com.google.gson.annotations.SerializedName

data class PostEmailCodeResource(
    val email: String,
    val userId: String
)