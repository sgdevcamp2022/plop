package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.request.user.PostLoginRequest
import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> = flow {
        try {
            val response = userRepository.postLogin(PostLoginRequest(email, password))
            when(response.code()) {
                200 -> {
                    val user = response.body()?.postLoginDto
                    userRepository.loginUser(
                        UserPref(
                            user?.accessToken ?: "",
                            user?.refreshToken ?: "",
                            "",
                            email
                        )
                    )
                    emit(Resource.Success(true))
                }
                401 -> {
                    Log.d("LoginUserCase", response.code().toString())
                    emit(Resource.Error("error ${response.code()}"))
                }
                else -> {
                    Log.d("LoginUserCase", response.code().toString())
                    emit(Resource.Error("error ${response.code()}"))
                }
            }
        }catch (e: Exception) {
            Log.d("LoginUserCase", e.message.toString())
            emit(Resource.Error("error"))
        }
    }
}