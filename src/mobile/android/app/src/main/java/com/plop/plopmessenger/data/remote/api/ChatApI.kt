package com.plop.plopmessenger.data.remote.api

import com.plop.plopmessenger.data.dto.request.chat.DeleteChatRoomRequest
import com.plop.plopmessenger.data.dto.request.chat.PostDmRoomRequest
import com.plop.plopmessenger.data.dto.request.chat.PostGroupRoomRequest
import com.plop.plopmessenger.data.dto.request.chat.PostInvitationRequest
import com.plop.plopmessenger.data.dto.response.chat.*
import com.plop.plopmessenger.data.remote.api.Constants.DELETE_CHATROOM
import com.plop.plopmessenger.data.remote.api.Constants.GET_CHATROOM_HISTORY
import com.plop.plopmessenger.data.remote.api.Constants.GET_CHATROOM_INFO
import com.plop.plopmessenger.data.remote.api.Constants.GET_CHATROOM_NEW_MESSAGE
import com.plop.plopmessenger.data.remote.api.Constants.GET_MY_ROOMS
import com.plop.plopmessenger.data.remote.api.Constants.POST_DM_CHATROOM
import com.plop.plopmessenger.data.remote.api.Constants.POST_GROUP_CHATROOM
import com.plop.plopmessenger.data.remote.api.Constants.POST_INVITATION
import retrofit2.Response
import retrofit2.http.*

interface ChatApI {

    @POST(POST_DM_CHATROOM)
    suspend fun postDmChatroom(
        @Body postDmRoomRequest: PostDmRoomRequest
    ): Response<PostDmRoomResponse>

    @POST(POST_GROUP_CHATROOM)
    suspend fun postGroupChatroom(
        @Body postGroupRoomRequest: PostGroupRoomRequest
    ): Response<PostGroupRoomResponse>

    @POST(POST_INVITATION)
    suspend fun postInvitation(
        @Body postInvitationRequest: PostInvitationRequest
    ): Response<PostInvitationResponse>

    @GET(GET_MY_ROOMS)
    suspend fun getMyRooms(): Response<GetMyRoomResponse>

    @DELETE(DELETE_CHATROOM)
    suspend fun deleteChatroom(
        @Path("roomid") roomid: String,
        @Body deleteChatRoomRequest: DeleteChatRoomRequest
    ): Response<DeleteChatRoomResponse>

    @GET(GET_CHATROOM_NEW_MESSAGE)
    suspend fun getChatroomNewMessage(
        @Path("roomid") roomid: String,
        @Path("readMsgId") readMsgId: String,
    ): Response<GetChatRoomNewMessageResponse>

    @GET(GET_CHATROOM_HISTORY)
    suspend fun getChatroomHistory(
        @Path("roomid") roomid: String
    ): Response<GetHistoryMessageResponse>

    @GET(GET_CHATROOM_INFO)
    suspend fun getChatRoomInfo(
        @Path("roomid") roomid: String
    ): Response<GetChatRoomInfoResponse>
}