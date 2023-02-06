package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toChatRoom
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFriendListUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(): Flow<Resource<List<People>>> = flow {
        try {
            emit(Resource.Loading())
            friendRepository.loadFriend().collect() { result ->
                emit(Resource.Success(result.map { it.toPeople() }))
            }
        } catch (e: IOException) {
            Log.d("GetFriendListUseCase", "IOException")
        } catch (e: Exception) {
            Log.d("GetFriendListUseCase", e.message.toString())
        }
    }
}