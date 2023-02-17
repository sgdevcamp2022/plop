package com.plop.plopmessenger.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.plop.plopmessenger.data.local.entity.Friend
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {
    @Query(
        "SELECT * FROM friends WHERE status = 1 ORDER BY nickname ASC"
    )
    fun loadFriend(): Flow<List<Friend>>

    @Query(
        "SELECT * FROM friends " +
                "WHERE status = 1 AND nickname LIKE :nickname " +
                "ORDER BY nickname ASC"
    )
    fun loadFriendByNickname(nickname: String): Flow<List<Friend>>

    @Query(
        "SELECT * FROM friends " +
                "WHERE status = 1 AND friend_id = :friendId "
    )
    suspend fun loadFriendById(friendId: String): List<Friend>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: Friend)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFriend(vararg friends: Friend)

    @Update
    suspend fun updateFriend(friend: Friend)

    @Query(
        "UPDATE friends SET status = 2 WHERE friend_id = :friendId"
    )
    suspend fun updateFriendStateToBlockById(friendId: String)

    @Query(
        "UPDATE friends SET status = 1 WHERE friend_id = :friendId"
    )
    suspend fun updateFriendStateToFriendById(friendId: String)

    @Update
    suspend fun updateAllFriend(vararg friends: Friend)
}