package com.plop.plopmessenger.data.local.dao

import androidx.room.*
import com.plop.plopmessenger.data.local.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query(
        "SELECT * FROM messages WHERE chatroom_id = :chatroomId ORDER BY created_at ASC"
    )
    fun loadChatMessage(chatroomId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(message: Message)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllMessage(vararg messages: Message)
}