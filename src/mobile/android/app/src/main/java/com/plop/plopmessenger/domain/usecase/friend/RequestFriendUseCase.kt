package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostFriendRequest
import com.plop.plopmessenger.data.dto.response.user.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestFriendUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(target: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = friendRepository.postFriendRequest(PostFriendRequest(target))
            if(response.isSuccessful) {
                emit(Resource.Success(true))
                Log.d("RequestFriendUseCase", "성공..성공이오..")
            }
            else {
                Log.d("RequestFriendUseCase", "error + ${response.code()}")
                emit(Resource.Error("error"))
            }
        }catch (e: Exception) {
            Log.d("RequestFriendUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}