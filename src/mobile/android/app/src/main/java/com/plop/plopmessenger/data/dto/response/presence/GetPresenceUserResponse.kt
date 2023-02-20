package com.plop.plopmessenger.data.dto.response.presence


import com.google.gson.annotations.SerializedName

data class GetPresenceUserResponse(
    val members: List<String> = emptyList()
)