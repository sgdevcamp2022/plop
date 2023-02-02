package com.plop.plopmessenger.domain.model

data class Member(
    val memberId: String,
    var profileImg: String,
    var nickname: String,
    var readMessage: String?
)