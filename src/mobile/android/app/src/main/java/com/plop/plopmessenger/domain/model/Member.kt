package com.plop.plopmessenger.domain.model

import com.plop.plopmessenger.data.local.entity.Member as MemberEntity

data class Member(
    val memberId: String,
    var profileImg: String,
    var nickname: String,
    var readMessage: String?
)

fun MemberEntity.toMember() = Member(
    memberId = this.memberId,
    profileImg = this.profileImg,
    nickname = this.nickname,
    readMessage = this.readMessage
)

fun People.toMember() = Member(
    memberId = this.peopleId,
    profileImg = this.profileImg,
    nickname = this.nickname,
    readMessage = null
)