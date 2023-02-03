package com.plop.plopmessenger.local.dao

import androidx.room.*
import com.plop.plopmessenger.local.entity.ChatRoom
import com.plop.plopmessenger.local.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomMemberImageDao {

    @Transaction
    @Query(
        "SELECT * " +
                "FROM chatroom ORDER BY chatroom.updated_at DESC "
    )
    fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage>>

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
    val images: List<Member>,
)