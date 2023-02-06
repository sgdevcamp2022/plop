package com.plop.plopmessenger.data.dto.response.chat


import com.google.gson.annotations.SerializedName
import com.plop.plopmessenger.data.dto.response.Message

data class GetHistoryMessageResponse(
    val getHistoryMessageDto: List<Message>
)