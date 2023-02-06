package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetFriendByNicknameListUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(nickname: String): Flow<Resource<List<People>>> = flow {
        try {
            emit(Resource.Loading())
            friendRepository.loadFriendByNickname(nickname).collect() { result ->
                emit(Resource.Success(result.map { it.toPeople() }))
            }
        } catch (e: IOException) {
            Log.d("GetFriendByNicknameListUseCase", "IOException")
        } catch (e: Exception) {
            Log.d("GetFriendByNicknameListUseCase", e.message.toString())
        }
    }
}