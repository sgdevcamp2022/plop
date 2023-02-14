package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.response.user.toFriend
import com.plop.plopmessenger.data.dto.response.user.toPeople
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRemoteFriendListUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        try {
            val response = friendRepository.getFriendList()
            when(response.code()) {
                200 -> {
                   if(response.body() != null) {
                       friendRepository.insertAllFriend(
                           response.body()?.profiles?.map { it.toFriend() }?: emptyList()
                       )
                   }
                    Log.d("GetRemoteFriendListUseCase", "성공..성공이요..")
                }
                else -> {
                    Log.d("GetRemoteFriendListUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d("GetRemoteFriendListUseCase", "error ${e.message}")
            emit(Resource.Error("error"))
        }
    }
}