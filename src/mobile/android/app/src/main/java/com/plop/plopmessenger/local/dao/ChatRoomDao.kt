package com.plop.plopmessenger.local.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {
    @Query(
        "SELECT title FROM chatroom WHERE chatroom_id = :chatroomId"
    )
    fun loadChatRoomTitle(chatroomId: String): Flow<String>

    @Query(
        "SELECT chatroom.chatroom_id FROM chatroom, members WHERE chatroom.chatroom_id =members.chatroom_id AND chatroom.type = 1 AND members.member_id = :friendId"
    )
    fun hasPersonalChatRoomByFriend(friendId: String): Flow<String?>
}