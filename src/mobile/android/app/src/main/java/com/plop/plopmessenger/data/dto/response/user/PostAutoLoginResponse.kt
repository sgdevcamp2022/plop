package com.plop.plopmessenger.data.dto.response.user


import com.google.gson.annotations.SerializedName

data class PostAutoLoginResponse(
    @SerializedName("data")
    val postAutoLoginDto: LoginDto,
    val message: String,
    val result: String
)