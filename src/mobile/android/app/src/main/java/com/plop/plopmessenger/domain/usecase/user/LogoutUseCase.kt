package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostAutoLoginRequest
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.deleteLogout()
            when(response.code()) {
                200 -> {
                    userRepository.logoutUser()
                    emit(Resource.Success(true))
                }
                else -> {
                    Log.d("LogoutUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d("LogoutUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}