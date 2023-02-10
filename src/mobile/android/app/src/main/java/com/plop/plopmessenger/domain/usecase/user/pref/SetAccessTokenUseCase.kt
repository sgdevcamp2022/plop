package com.plop.plopmessenger.domain.usecase.user.pref

import com.plop.plopmessenger.domain.repository.UserRepository
import javax.inject.Inject

class SetAccessTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(accessToken: String) {
        userRepository.setAccessToken(accessToken)
    }
}