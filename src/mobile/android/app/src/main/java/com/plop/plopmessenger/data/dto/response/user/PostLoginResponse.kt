package com.plop.plopmessenger.data.dto.response.user


import com.google.gson.annotations.SerializedName

data class PostLoginResponse(
    @SerializedName("data")
    val postLoginDto: LoginDto,
    val message: String,
    val result: String
)