package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "members",
    primaryKeys = ["chatroom_id", "member_id"],
    foreignKeys = [
        ForeignKey(
            entity = Message::class,
            parentColumns = ["message_id"],
            childColumns = ["read_message"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = ["chatroom_id"],
            childColumns = ["chatroom_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Member(
    @ColumnInfo(name = "chatroom_id")
    val chatroomId: String,
    @ColumnInfo(name = "member_id")
    val memberId: String,
    val nickname: String,
    @ColumnInfo(name = "profile_img")
    var profileImg: String,
    @ColumnInfo(name = "read_message")
    var readMessage: String? = null,
)


