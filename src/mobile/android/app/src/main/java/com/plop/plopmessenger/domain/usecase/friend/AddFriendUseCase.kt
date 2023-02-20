package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostFriendAcceptRequest
import com.plop.plopmessenger.data.local.entity.toFriend
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddFriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(friend: People): Flow<Resource<Boolean>> = flow {
        try {
            friendRepository.insertFriend(friend.toFriend())
            emit(Resource.Success(true))
        }catch (e: Exception) {
            Log.d("AddFriendUseCase", e.message.toString())
            emit(Resource.Error("error"))
        }
    }
}