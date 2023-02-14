package com.plop.plopmessenger.presentation.navigation

import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.google.gson.Gson
import com.plop.plopmessenger.presentation.component.BottomBarTabs

object LoginDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signup"
    const val FIND_PASSWORD_ROUTE = "findPassword"
}

object BottomBarDestinations {
    const val CHATS_ROUTE = "chats"
    const val PEOPLE_ROUTE = "people"
    const val SETTING_ROUTE = "setting"
}

object MainDestinations {
    const val MAIN_ROUTE = "main"
    const val PEOPLE_GRAPH_ROUTE = "peopleGraph"
    const val CHAT_ROUTE = "chat"
    const val NEW_CHAT_ROUTE = "newChat"
    const val CHAT_INFO_ROUTE = "chatInfo"
    const val ADD_CHAT_MEMBER_ROUTE = "addChatMemberRoute"
    const val ADD_CHAT_ROUTE = "addChat"
    const val ADD_CHAT_GROUP_ROUTE = "addGroupChat"
    const val ADD_PEOPLE_ROUTE = "addPeople"
    const val MODIFY_PROFILE = "modifyProfile"
    const val SETTING_GRAPH_ROUTE = "settingGraph"
}

object DestinationID {
    const val CHAT_ID = "chatId"
    const val PEOPLE_LIST = "peopleList"
}

class LoginNavigationAction(navController: NavController) {
    val upPress:() -> Unit = {
        navController.navigateUp()
    }

    val navigateToSignUp:() -> Unit = {
        navController.navigate(LoginDestinations.SIGN_UP_ROUTE)
    }

    val navigateToFindPassword:() -> Unit = {
        navController.navigate(LoginDestinations.FIND_PASSWORD_ROUTE)
    }
}



class MainNavigationAction(val navController: NavController) {
    private val currentRoute: String?
        get() = navController.currentDestination?.route

    val upPress:() -> Unit = {
        navController.navigateUp()
    }

    val navigateToBottomRoute: (String) -> Unit = { route ->
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(BottomBarDestinations.CHATS_ROUTE) {
                    saveState = true
                }
            }
        }
    }

    val navigateToChatInfo: (String, NavBackStackEntry) -> Unit = { chatId, from ->
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.CHAT_INFO_ROUTE}/$chatId")
        }
    }

    val navigateToAddMember: (String, NavBackStackEntry) -> Unit = { chatId, from ->
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.ADD_CHAT_MEMBER_ROUTE}/$chatId")
        }
    }

    val navigateToChat: (String, NavBackStackEntry) -> Unit = { chatId, from ->
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.CHAT_ROUTE}/$chatId")
        }
    }

    //새로 생성된 Chat
    val navigateToNewChat: (PeopleParcelableModel) -> Unit = {
        val peopleList = Uri.encode(Gson().toJson(it))
        navController.navigate("${MainDestinations.NEW_CHAT_ROUTE}/${peopleList}"){
            popUpTo(BottomBarDestinations.CHATS_ROUTE)
        }
    }

    //그룹채팅에 멤버가 추가되었을 때?
    val navigateToUpdateGroupChat: (String, PeopleParcelableModel) -> Unit = { chatId, people->
        val peopleList = Uri.encode(Gson().toJson(people))
        navController.navigate("${MainDestinations.CHAT_ROUTE}/$chatId/${peopleList}") {
            popUpTo(BottomBarDestinations.CHATS_ROUTE)
        }
    }

    val navigateToAddChat: () -> Unit = {
        navController.navigate(MainDestinations.ADD_CHAT_ROUTE)
    }

    val navigateToAddGroupChat: () -> Unit = {
        navController.navigate(MainDestinations.ADD_CHAT_GROUP_ROUTE)
    }

    val navigateToAddPeople: () -> Unit = {
        navController.navigate(MainDestinations.ADD_PEOPLE_ROUTE)
    }

    val navigateToModifyProfile: () -> Unit = {
        navController.navigate(MainDestinations.MODIFY_PROFILE)
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED