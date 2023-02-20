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
    suspend operator fun invoke(): Resource<Boolean> {
        try {
            val response = friendRepository.getFriendList()
            if(response.isSuccessful) {
                if(response.body() != null) {
                    friendRepository.insertAllFriend(
                        response.body()?.profiles?.map { it.toFriend() }?: emptyList()
                    )
                }
                Log.d("GetRemoteFriendListUseCase", "성공..성공이요..")
                return Resource.Success(true)
            } else{
                Log.d("GetRemoteFriendListUseCase", "error")
                return Resource.Error("error")
            }
        }catch (e: Exception) {
            Log.d("GetRemoteFriendListUseCase", "error ${e.message}")
            return Resource.Error("error")
        }
    }
}