package com.plop.plopmessenger.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

/**
 * type
 * 1 -> TEXT
 * 2 -> IMAGE
 * 3 -> VIDEO
 * 4 -> ENTER
 */
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["chatroom_id", "member_id"],
            childColumns = ["chatroom_id", "message_from_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Message(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val messageId: String,
    @ColumnInfo(name = "message_from_id")
    val messageFromID: String,
    @ColumnInfo(name = "chatroom_id")
    val chatroomId: String,
    var content: String,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime,
    val type: Int
)
