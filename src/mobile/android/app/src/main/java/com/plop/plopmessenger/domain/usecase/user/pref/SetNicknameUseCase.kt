package com.plop.plopmessenger.domain.usecase.user.pref

import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.presentation.state.UserState
import javax.inject.Inject

class SetNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(nickname: String) {
        userRepository.setNickname(nickname)
        UserState.nickname = nickname
    }
}