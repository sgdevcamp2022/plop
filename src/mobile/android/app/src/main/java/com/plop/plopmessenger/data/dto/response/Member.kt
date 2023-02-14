package com.plop.plopmessenger.data.dto.response


import com.plop.plopmessenger.data.local.entity.Member as MemberEntity
import java.time.LocalDateTime

data class Member(
    val enteredAt: String,
    val lastReadMsgId: String?,
    val userId: String
)

fun Member.toMember(roomId: String) = MemberEntity(
    chatroomId = roomId,
    memberId = this.userId,
    nickname = "",
    profileImg = "",
    readMessage = this.lastReadMsgId
)