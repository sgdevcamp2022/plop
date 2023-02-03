package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


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

