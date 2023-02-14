package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostPasswordNewRequest
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        target: String
    ): Flow<Resource<People?>> = flow {
        try {
            val response = userRepository.getUserProfile(target)
            if(response.isSuccessful) {
                emit(Resource.Success(response.body()?.user?.toPeople()))
            } else {
                Log.d("GetUserProfileUseCase", "error")
                emit(Resource.Error("error"))
            }
        }catch (e: Exception) {
            Log.d(" GetUserProfileUseCase", "error ${e.message}")
            emit(Resource.Error("error"))
        }
    }
}