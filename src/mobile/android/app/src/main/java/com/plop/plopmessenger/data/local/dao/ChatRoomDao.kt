package com.plop.plopmessenger.data.local.dao

import androidx.room.*
import com.plop.plopmessenger.data.local.entity.ChatRoom
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ChatRoomDao {
    @Query(
        "SELECT title FROM chatroom WHERE chatroom_id = :chatroomId"
    )
    fun loadChatRoomTitle(chatroomId: String): Flow<String>

    @Query(
        "SELECT chatroom_id FROM chatroom WHERE chatroom_id = :chatroomId"
    )
    suspend fun hasChatRoomById(chatroomId: String): String?

    @Query(
        "SELECT chatroom_id FROM chatroom"
    )
    suspend fun loadChatRoomIdList(): List<String>

    @Query(
        "SELECT chatroom.chatroom_id FROM chatroom, members WHERE chatroom.chatroom_id =members.chatroom_id AND chatroom.type = 1 AND members.member_id = :friendId"
    )
    suspend fun hasPersonalChatRoomByFriend(friendId: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRoom(chatRoom: ChatRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChatRoom(vararg chatRooms: ChatRoom)

    @Update
    suspend fun updateChatRoom(chatroom: ChatRoom)

    @Update
    suspend fun updateChatroomAll(vararg chatrooms: ChatRoom)

    @Query(
        "UPDATE chatroom SET title = :title WHERE chatroom_id = :chatroomId"
    )
    suspend fun updateChatRoomTitleById(chatroomId: String, title: String)

    @Query(
        "UPDATE chatroom SET unread = :unread WHERE chatroom_id = :chatroomId"
    )
    suspend fun updateChatRoomUnreadById(chatroomId: String, unread: Int)

    @Query(
        "UPDATE chatroom SET unread = :unread + unread WHERE chatroom_id = :chatroomId"
    )
    suspend fun plusChatRoomUnreadById(chatroomId: String, unread: Int)

    @Query(
        "UPDATE chatroom SET content = :content , updated_at = :updatedAt WHERE chatroom_id = :chatroomId"
    )
    suspend fun updateChatRoomContentById(chatroomId: String, content: String, updatedAt: LocalDateTime)

    @Query(
        "DELETE FROM chatroom WHERE chatroom_id = :chatroomId"
    )
    fun deleteChatRoom(chatroomId: String)

    @Query("DELETE FROM chatroom")
    suspend fun deleteChatRoomData()
}