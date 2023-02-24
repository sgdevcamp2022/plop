package com.plop.plopmessenger.data.dto.response.user



data class LoginDto(
    val accessExpire: String,
    val accessToken: String,
    val refreshExpire: String,
    val refreshToken: String
)