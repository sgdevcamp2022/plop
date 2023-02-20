package com.plop.plopmessenger.domain.usecase.user.pref

import com.plop.plopmessenger.data.pref.model.UserPref
import com.plop.plopmessenger.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(): Flow<String> {
        return userRepository.getEmail()
    }
}