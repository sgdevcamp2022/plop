package com.plop.plopmessenger.domain.model

import com.plop.plopmessenger.data.local.entity.Friend

data class People(
    val peopleId: String,
    var nickname: String,
    var profileImg: String,
    val email: String,
    var status: PeopleStatusType
)

enum class PeopleStatusType {
    FRIEND, BLOCK, REQUEST, NONE
}

fun Friend.toPeople(): People = People(
    peopleId = this.friendId,
    nickname = this.nickname,
    profileImg = this.profileImg,
    email = this.email,
    status = when(this.status) {
        1 -> PeopleStatusType.FRIEND
        2 -> PeopleStatusType.BLOCK
        3 -> PeopleStatusType.REQUEST
        else -> PeopleStatusType.NONE
    }
)

fun Member.toPeople(): People = People(
    peopleId = this.memberId,
    nickname = this.nickname,
    profileImg = this.profileImg,
    email = "",
    status = PeopleStatusType.NONE
)