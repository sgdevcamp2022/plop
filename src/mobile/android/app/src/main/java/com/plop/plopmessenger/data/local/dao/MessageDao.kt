package com.plop.plopmessenger.data.local.dao

import androidx.room.*
import com.plop.plopmessenger.data.local.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query(
        "SELECT * FROM messages WHERE chatroom_id = :chatroomId ORDER BY created_at DESC LIMIT :size OFFSET :page * :size"
    )
    suspend fun loadChatMessage(chatroomId: String, page: Int, size: Int): List<Message>

    @Query(
        "SELECT * FROM messages WHERE chatroom_id = :chatroomId ORDER BY created_at DESC LIMIT 1 "
    )
    fun loadChatFirstMessage(chatroomId: String): Flow<Message?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMessage(vararg messages: Message)
}