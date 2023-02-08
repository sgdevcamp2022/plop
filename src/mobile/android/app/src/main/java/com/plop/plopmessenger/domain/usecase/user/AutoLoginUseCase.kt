package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostAutoLoginRequest
import com.plop.plopmessenger.data.dto.request.user.PostLoginRequest
import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.postAutoLogin(PostAutoLoginRequest(email))
            when(response.code()) {
                200 -> {
                    val user = response.body()?.postAutoLoginDto
                    userRepository.setAccessToken(user?.accessToken ?: "")
                    userRepository.setRefreshToken(user?.refreshToken ?: "")
                    emit(Resource.Success(true))
                }
                else -> {
                    Log.d("AutoLoginUseCase", "error")
                    emit(Resource.Error("error"))
                }
            }
        }catch (e: Exception) {
            Log.d("AutoLoginUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}