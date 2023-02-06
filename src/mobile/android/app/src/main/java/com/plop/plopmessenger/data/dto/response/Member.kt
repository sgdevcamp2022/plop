package com.plop.plopmessenger.data.dto.response


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Member(
    val enteredAt: LocalDateTime,
    val lastReadMsgId: String?,
    val userId: String
)