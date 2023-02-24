package com.plop.plopmessenger.domain.usecase.user.pref

import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.presentation.state.ThemeConstants
import com.plop.plopmessenger.presentation.state.UserState
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(mode: Boolean) {
        userRepository.setThemeMode(mode)
        UserState.mode = if(mode) ThemeConstants.DARK else ThemeConstants.LIGHT
    }
}