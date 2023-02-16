package com.plop.plopmessenger.domain.repository

import com.plop.plopmessenger.data.dto.request.chat.*
import com.plop.plopmessenger.data.dto.response.chat.*
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import com.plop.plopmessenger.data.local.entity.ChatRoom
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*

interface ChatRoomRepository {
    fun loadChatRoomTitle(chatroomId: String): Flow<String>
    fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage?>>
    suspend fun loadChatRoomIdList(): List<String>
    fun loadChatRoomAndMemberById(chatroomId: String): ChatRoomMemberImage
    suspend fun hasPersonalChatRoomByFriend(friendId: String): String?
    suspend fun hasChatRoomById(chatroomId: String): Boolean
    suspend fun insertChatRoom(chatRoom: ChatRoom)
    suspend fun insertAllChatRoom(chatRooms: List<ChatRoom>)
    suspend fun updateChatRoom(chatroom: ChatRoom)
    suspend fun updateChatroomAll(chatrooms: List<ChatRoom>)
    suspend fun updateChatRoomTitleById(chatroomId: String, title: String)
    suspend fun updateChatRoomUnreadById(chatroomId: String, unread: Int)
    suspend fun plusChatRoomUnreadById(chatroomId: String, unread: Int)
    suspend fun updateChatRoomContentById(chatroomId: String, content: String, updatedAt: LocalDateTime)
    suspend fun deleteChatRoom(chatroomId: String)
    suspend fun postDmChatroom(postDmRoomRequest: PostDmRoomRequest): Response<PostDmRoomResponse>
    suspend fun postGroupChatroom(postGroupRoomRequest: PostGroupRoomRequest): Response<PostGroupRoomResponse>
    suspend fun postInvitation(postInvitationRequest: PostInvitationRequest): Response<PostInvitationResponse>
    suspend fun getMyRooms(): Response<GetMyRoomResponse>
    suspend fun deleteChatroom(roomid: String): Response<DeleteChatRoomResponse>
    suspend fun getChatroomNewMessage(roomid: String, readMsgId: String): Response<GetChatRoomNewMessageResponse>
    suspend fun getChatroomHistory(roomid: String): Response<GetHistoryMessageResponse>
    suspend fun getChatRoomInfo(roomid: String): Response<GetChatRoomInfoResponse>
    suspend fun postMessage(postMessageRequest: PostMessageRequest): Response<Void>
}