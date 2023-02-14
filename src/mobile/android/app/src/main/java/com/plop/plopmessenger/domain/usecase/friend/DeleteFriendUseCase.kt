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
    operator fun invoke(target: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = friendRepository.deleteFriendRequest(DeleteFriendRequestRequest(target))
            if(response.isSuccessful) {
                emit(Resource.Success(true))
                Log.d("DeleteFriendUseCase", "성공..성공이오..")
            } else {
                Log.d("DeleteFriendUseCase", "실패..실패요.... + ${response.code()}")
            }
        }catch (e: Exception) {
            Log.d("DeleteFriendUseCase", e.message.toString())
            emit(Resource.Error("error"))
        }
    }
}