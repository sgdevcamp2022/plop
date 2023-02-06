package com.plop.plopmessenger.domain.usecase.chatroom

import javax.inject.Inject

data class ChatRoomUseCase @Inject constructor(
    val getLocalChatRoomListUseCase: GetLocalChatRoomListUseCase,
    val getChatRoomInfoUseCase: GetChatRoomInfoUseCase,
    val getChatRoomIdByPeopleIdUseCase: GetChatRoomIdByPeopleIdUseCase
)