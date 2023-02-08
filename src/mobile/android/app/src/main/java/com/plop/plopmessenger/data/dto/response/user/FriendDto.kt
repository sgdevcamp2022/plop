package com.plop.plopmessenger.data.dto.response.user

import com.plop.plopmessenger.data.local.entity.Friend
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.PeopleStatusType

data class FriendDto(
    val userid: String,
    val email: String,
    val profile: UserProfileDto
)

fun FriendDto.toFriend() = Friend(
    friendId = this.userid,
    nickname = this.profile.nickname,
    profileImg = this.profile.image,
    email = this.email,
    status = 4
)

fun FriendDto.toPeople() = People(
    peopleId = this.userid,
    nickname = this.profile.nickname,
    profileImg = this.profile.image,
    email = this.email,
    status = PeopleStatusType.NONE
)