package com.plop.plopmessenger.data.local.dao

import androidx.room.*
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomMemberImageDao {

    @Transaction
    @Query(
        "SELECT * " +
                "FROM chatroom ORDER BY chatroom.updated_at DESC "
    )
    fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage?>>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM chatroom  WHERE chatroom_id = :chatroomId"
    )
    fun loadChatRoomAndMemberById(chatroomId: String): Flow<ChatRoomMemberImage>
}

data class ChatRoomMemberImage(
    @Embedded val chatroom: ChatRoom,

    @Relation(
        parentColumn = "chatroom_id",
        entityColumn = "chatroom_id"
    )
    val members: List<Member>,
)