package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.PeopleStatusType


/**
 * status
 * 1 -> Friend
 * 2 -> Block
 * 3 -> Request
 */
@Entity(tableName = "friends")
data class Friend(
    @PrimaryKey
    @ColumnInfo(name = "friend_id")
    val friendId: String,
    var nickname: String,
    @ColumnInfo(name = "profile_img")
    var profileImg: String,
    var status: Int,
    val email: String
)

fun People.toFriend(): Friend = Friend(
    friendId = this.peopleId,
    nickname = this.nickname,
    profileImg = this.profileImg,
    email = this.email,
    status = when(this.status) {
        PeopleStatusType.FRIEND -> 1
        PeopleStatusType.BLOCK -> 2
        PeopleStatusType.REQUEST -> 3
        else -> 4
    }
)