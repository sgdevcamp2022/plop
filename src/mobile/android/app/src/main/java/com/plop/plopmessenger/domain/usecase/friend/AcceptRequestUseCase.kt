package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostFriendAcceptRequest
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AcceptRequestUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(target: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = friendRepository.postFriendAcceptRequest(PostFriendAcceptRequest(target))
            if(response.isSuccessful) {
                Log.d("AcceptRequestUseCase", "성공..성공이오..")
                emit(Resource.Success(true))
            } else {
                Log.d("AcceptRequestUseCase","실패..실패오..")
                emit(Resource.Error("실패..실패요..."))
            }
        }catch (e: Exception) {
            Log.d("AcceptRequestUseCase", e.message.toString())
            emit(Resource.Error("error"))
        }
    }
}