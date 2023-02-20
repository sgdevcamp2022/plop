package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRejectRequest
import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRequestRequest
import com.plop.plopmessenger.data.dto.request.user.PostFriendRequest
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RejectRequestUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(target: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = friendRepository.deleteFriendRejectRequest(DeleteFriendRejectRequest(target))
            if(response.isSuccessful) {
                Log.d("RefuseRequestUseCase", "성공..성공이오..")
                emit(Resource.Success(true))
            } else {
                Log.d("RefuseRequestUseCase","실패..실패오..")
                emit(Resource.Error("실패..실패요..."))
            }
        }catch (e: Exception) {
            Log.d("RefuseRequestUseCase", e.message.toString())
            emit(Resource.Error("error"))
        }
    }
}