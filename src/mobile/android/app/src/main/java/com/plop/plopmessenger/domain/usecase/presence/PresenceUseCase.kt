package com.plop.plopmessenger.domain.usecase.presence

import javax.inject.Inject

data class PresenceUseCase @Inject constructor(
    val getPresenceUserUseCase: GetPresenceUserUseCase,
    val putOnUseCase: PutOnUseCase,
    val putOffUseCase: PutOffUseCase
)