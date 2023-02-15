package com.plop.plopmessenger.data.remote.api

object Constants {
    const val BASE_URL = "http://3.39.130.186:8000"
    const val POST_DM_CHATROOM = "/chatting/room/v1/dm-creation"
    const val POST_GROUP_CHATROOM = "/chatting/room/v1/group-creation"
    const val POST_INVITATION = "/chatting/room/v1/invitation"
    const val GET_MY_ROOMS = "/chatting/room/v1/my-rooms"
    const val DELETE_CHATROOM = "/chatting/room/v1/out/{roomid}"
    const val GET_CHATROOM_NEW_MESSAGE = "/chatting/v1/new-message"
    const val GET_CHATROOM_HISTORY = "/chatting/v1/history-message"
    const val GET_CHATROOM_INFO = "/chatting/room/v1/info"
    const val POST_MESSAGE = "/chatting/v1/message"

    const val POST_LOGIN = "/auth/v1/login"
    const val POST_AUTO_LOGIN = "/auth/v1/reissue"
    const val DELETE_LOGOUT = "/auth/v1/logout"
    const val POST_SIGN_UP = "/auth/v1/signup"
    const val POST_EMAIL_CODE = "/auth/v1/email/code"
    const val POST_EMAIL_VERIFY = "/auth/v1/email/verify"
    const val DELETE_WITHDRAWAL = "/auth/v1/withdrawal"
    const val POST_PASSWORD_NEW = "/auth/v1/password/new"
    const val GET_USER_PROFILE = "/user/v1/profile"
    const val PUT_USER_PROFILE = "/user/v1/profile"
    const val GET_SEARCH_USER = "/user/v1/search"

    const val GET_FRIEND_LIST = "/user/v1/friend"
    const val GET_FRIEND_REQUEST_LIST = "/user/v1/friend/request"
    const val GET_FRIEND_RESPONSE_LIST = "/user/v1/friend/response"
    const val POST_FRIEND_REQUEST = "/user/v1/friend/request"
    const val DELETE_FRIEND_REQUEST = "/user/v1/friend/request"
    const val DELETE_FRIEND = "/user/v1/friend/{friendid}"
    const val POST_FRIEND_REQUEST_ACCEPT = "/user/v1/friend/response"
    const val DELETE_FRIEND_REQUEST_REJECT = "/user/v1/friend/response"

    const val PAGE_SIZE = 10
}