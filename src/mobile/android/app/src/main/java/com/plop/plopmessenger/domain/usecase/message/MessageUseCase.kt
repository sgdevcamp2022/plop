package com.plop.plopmessenger.domain.usecase.message

import javax.inject.Inject

data class MessageUseCase @Inject constructor(
    val getLocalMessageListUseCase: GetLocalMessageListUseCase,
    val getFirstMessageUseCase: GetFirstMessageUseCase
)
