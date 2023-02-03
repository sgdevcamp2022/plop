package com.plop.plopmessenger.util

fun getChatRoomTitle(members: List<String>): String{
    return members[0]+ members[1] + members[2]
}