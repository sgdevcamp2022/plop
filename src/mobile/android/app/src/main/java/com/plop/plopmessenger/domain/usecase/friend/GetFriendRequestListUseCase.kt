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
            val response = friendRepository.getFriendList()
            when(response.code()) {
                200 -> {
                    if(response.body() != null) {
                        emit(Resource.Success(response.body()!!.profiles.map { it.toPeople() }))
                    }
                }
                else -> {
                    Log.d("GetFriendRequestListUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d("GetFriendRequestListUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}