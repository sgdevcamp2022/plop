package com.plop.plopmessenger.presentation.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.plop.plopmessenger.presentation.component.BottomBarTabs

object LoginDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signup"
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
    const val CHAT_INFO_ROUTE = "chatInfo"
    const val ADD_CHAT_MEMBER_ROUTE = "addChatMemberRoute"
    const val ADD_CHAT_ROUTE = "addChat"
    const val ADD_CHAT_GROUP_ROUTE = "addGroupChat"
    const val ADD_PEOPLE_ROUTE = "addPeople"
}


object DestinationID {
    const val CHAT_ID = "chatId"
}

class LoginNavigationAction(navController: NavController) {
    val upPress:() -> Unit = {
        navController.navigateUp()
    }

    val navigateToSignUp:() -> Unit = {
        navController.navigate(LoginDestinations.SIGN_UP_ROUTE)
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

    val navigateToChat: (String, NavBackStackEntry) -> Unit = { chatId, from ->
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.CHAT_ROUTE}/$chatId")
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

    //새로 생성된 Chat
    val navigateToNewChat: () -> Unit = {
        navController.navigate(MainDestinations.CHAT_ROUTE){
            popUpTo(BottomBarDestinations.CHATS_ROUTE)
        }
    }

    //그룹채팅에 멤버가 추가되었을 때?
    val navigateToUpdateGroupChat: (String, NavBackStackEntry) -> Unit = { chatId, from ->
        navController.navigate("${MainDestinations.CHAT_ROUTE}/$chatId") {
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
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED