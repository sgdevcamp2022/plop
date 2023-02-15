package com.plop.plopmessenger.data.repository

import com.plop.plopmessenger.data.dto.request.chat.*
import com.plop.plopmessenger.data.dto.response.chat.*
import com.plop.plopmessenger.data.local.dao.ChatRoomDao
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImage
import com.plop.plopmessenger.data.local.dao.ChatRoomMemberImageDao
import com.plop.plopmessenger.data.local.entity.ChatRoom
import com.plop.plopmessenger.data.remote.api.ChatApi
import com.plop.plopmessenger.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val chatRoomDao: ChatRoomDao,
    private val chatroomMemberImageDao: ChatRoomMemberImageDao,
    private val chatApi: ChatApi
): ChatRoomRepository {
    override fun loadChatRoomTitle(chatroomId: String): Flow<String> {
        return chatRoomDao.loadChatRoomTitle(chatroomId)
    }

    override fun loadChatRoomAndMessage(): Flow<List<ChatRoomMemberImage>> {
        return chatroomMemberImageDao.loadChatRoomAndMessage()
    }

    override fun loadChatRoomIdList(): Flow<List<String>> {
        return chatRoomDao.loadChatRoomIdList()
    }

    override fun loadChatRoomAndMemberById(chatroomId: String): Flow<ChatRoomMemberImage> {
        return chatroomMemberImageDao.loadChatRoomAndMemberById(chatroomId)
    }

    override fun hasPersonalChatRoomByFriend(friendId: String): Flow<String?> {
        return chatRoomDao.hasPersonalChatRoomByFriend(friendId)
    }

    override suspend fun insertChatRoom(chatRoom: ChatRoom) {
        return chatRoomDao.insertChatRoom(chatRoom)
    }

    override suspend fun insertAllChatRoom(chatRooms: List<ChatRoom>) {
        return chatRoomDao.insertAllChatRoom(*chatRooms.toTypedArray())
    }

    override suspend fun updateChatRoom(chatroom: ChatRoom) {
        return chatRoomDao.updateChatRoom(chatroom)
    }

    override suspend fun updateChatroomAll(chatrooms: List<ChatRoom>) {
        return chatRoomDao.updateChatroomAll(*chatrooms.toTypedArray())
    }

    override suspend fun updateChatRoomTitleById(chatroomId: String, title: String) {
        return chatRoomDao.updateChatRoomTitleById(chatroomId, title)
    }

    override suspend fun updateChatRoomUnreadById(chatroomId: String, unread: Int) {
        return chatRoomDao.updateChatRoomUnreadById(chatroomId, unread)
    }

    override suspend fun plusChatRoomUnreadById(chatroomId: String, unread: Int) {
        return chatRoomDao.plusChatRoomUnreadById(chatroomId, unread)
    }

    override suspend fun updateChatRoomContentById(chatroomId: String, content: String, updatedAt: LocalDateTime) {
        return chatRoomDao.updateChatRoomContentById(chatroomId, content, updatedAt)
    }

    override suspend fun deleteChatRoom(chatroomId: String) {
        return chatRoomDao.deleteChatRoom(chatroomId)
    }

    override suspend fun postDmChatroom(postDmRoomRequest: PostDmRoomRequest): Response<PostDmRoomResponse> {
        return chatApi.postDmChatroom(postDmRoomRequest)
    }

    override suspend fun postGroupChatroom(postGroupRoomRequest: PostGroupRoomRequest): Response<PostGroupRoomResponse> {
        return chatApi.postGroupChatroom(postGroupRoomRequest)
    }

    override suspend fun postInvitation(postInvitationRequest: PostInvitationRequest): Response<PostInvitationResponse> {
        return chatApi.postInvitation(postInvitationRequest)
    }

    override suspend fun getMyRooms(): Response<GetMyRoomResponse> {
        return chatApi.getMyRooms()
    }

    override suspend fun deleteChatroom(
        roomid: String,
        deleteChatRoomRequest: DeleteChatRoomRequest
    ): Response<DeleteChatRoomResponse> {
        return chatApi.deleteChatroom(roomid, deleteChatRoomRequest)
    }

    override suspend fun getChatroomNewMessage(roomid: String, readMsgId: String): Response<GetChatRoomNewMessageResponse> {
        return chatApi.getChatroomNewMessage(roomid = roomid, readMsgId = readMsgId)
    }

    override suspend fun getChatroomHistory(
        roomid: String
    ): Response<GetHistoryMessageResponse> {
        return chatApi.getChatroomHistory(roomid)
    }

    override suspend fun getChatRoomInfo(roomid: String): Response<GetChatRoomInfoResponse> {
        return return chatApi.getChatRoomInfo(roomid)
    }

    override suspend fun postMessage(postMessageRequest: PostMessageRequest): Response<Void> {
        return chatApi.postMessage(postMessageRequest)
    }
}