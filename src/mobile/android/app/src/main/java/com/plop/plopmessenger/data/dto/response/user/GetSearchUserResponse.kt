package com.plop.plopmessenger.data.dto.response.user

import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.PeopleStatusType

data class GetSearchUserResponse(
    val email: String,
    val userid: String,
    val profile: UserProfileDto,
    val message: String
)

fun GetSearchUserResponse.toPeople() = People(
    email = this.email,
    peopleId = this.userid,
    nickname = this.profile.nickname,
    profileImg = this.profile.image,
    status = PeopleStatusType.NONE
)