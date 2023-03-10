package com.plop.plopmessenger.domain.usecase.chatroom

import com.plop.plopmessenger.domain.usecase.friend.DeleteFriendsUseCase
import javax.inject.Inject

data class ChatRoomUseCase @Inject constructor(
    val getLocalChatRoomListUseCase: GetLocalChatRoomListUseCase,
    val getChatRoomInfoUseCase: GetChatRoomInfoUseCase,
    val getChatRoomIdByPeopleIdUseCase: GetChatRoomIdByPeopleIdUseCase,
    val createDmChatRoomUseCase: CreateDmChatRoomUseCase,
    val createGroupChatRoomUseCase: CreateGroupChatRoomUseCase,
    val inviteMemberUseCase: InviteMemberUseCase,
    val getNewChatRoomMessageUseCase: GetNewChatRoomMessageUseCase,
    val getMessageHistoryUseCase: GetMessageHistoryUseCase,
    val getRemoteChatRoomInfoUseCase: GetRemoteChatRoomInfoUseCase,
    val getRemoteChatRoomUseCase: GetRemoteChatRoomUseCase,
    val leaveChatRoomUseCase: LeaveChatRoomUseCase,
    val deleteChatRoomsUseCase: DeleteChatRoomsUseCase
)