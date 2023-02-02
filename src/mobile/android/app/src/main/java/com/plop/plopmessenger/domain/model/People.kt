package com.plop.plopmessenger.domain.model

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