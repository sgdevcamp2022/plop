package com.plop.plopmessenger.domain.usecase.friend

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.DeleteFriendRequestRequest
import com.plop.plopmessenger.data.dto.response.user.toPeople
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.repository.FriendRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFriendRequestListUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    operator fun invoke(): Flow<Resource<List<People>>> = flow {
        try {
            val response = friendRepository.getFriendRequestList()
            if(response.isSuccessful) {
                if(response.body() != null) {
                    emit(Resource.Success(response.body()?.profiles?.map { it.toPeople() }?: emptyList()))
                }
                Log.d("GetFriendRequestListUseCase", "성공..성공이오...!")
            } else {
                Log.d("GetFriendRequestListUseCase", "실패...실패오..")
                emit(Resource.Error("error"))
            }
        }catch (e: Exception) {
            Log.d("GetFriendRequestListUseCase", "${e.message}")
            emit(Resource.Error("error"))
        }
    }
}