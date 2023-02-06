package com.plop.plopmessenger.data.dto.response.user


data class LoginDto(
    val accessExpire: Long,
    val accessToken: String,
    val refreshExpire: Long,
    val refreshToken: String
)