package com.plop.plopmessenger.data.dto.response.user

import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.People


data class GetSearchUserResponse(
    @SerializedName("data")
    val people: List<People>,
    val result: String,
    val message: String
)
