package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRequestRequest
import com.plop.plopmessenger.data.dto.request.user.PostFriendRequest
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteFriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(friendId: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = friendRepository.deleteFriendRequest(DeleteFriendRequestRequest(friendId))
            when(response.code()) {
                200 -> {
                    emit(Resource.Success(true))
                }
                else -> {
                    Log.d("DeleteFriendUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d("DeleteFriendUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}