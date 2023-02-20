package com.plop.plopmessenger.data.dto.request.user


data class PostLoginRequest(
    val idOrEmail: String,
    val password: String
)