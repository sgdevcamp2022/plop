package com.plop.plopmessenger.presentation.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plop.plopmessenger.presentation.navigation.*
import com.plop.plopmessenger.presentation.screen.main.*

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.MAIN_ROUTE,
    navigationAction: MainNavigationAction,
    navigateToLogin:() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            route = MainDestinations.MAIN_ROUTE,
            startDestination = BottomBarDestinations.CHATS_ROUTE
        ) {
            bottomNavGraph(navigationAction, navigateToLogin)
            chatGraph(navigationAction, navigateToLogin)
        }
    }
}

private fun NavGraphBuilder.bottomNavGraph(
    navigationAction: MainNavigationAction,
    navigateToLogin:() -> Unit
) {
    composable(BottomBarDestinations.CHATS_ROUTE) { from ->
        ChatsScreen(
            navigateToChat = { chatId -> navigationAction.navigateToChat(chatId, from)},
            navigateToAddChat = { navigationAction.navigateToAddChat() }
        )
    }

    navigation(
        route = MainDestinations.PEOPLE_GRAPH_ROUTE,
        startDestination = BottomBarDestinations.PEOPLE_ROUTE
    ) {
        peopleGraph(navigationAction, navigateToLogin)
    }

    navigation(
        route = MainDestinations.SETTING_GRAPH_ROUTE,
        startDestination = BottomBarDestinations.SETTING_ROUTE
    ) {
        settingGraph(navigationAction, navigateToLogin)
    }

}

private fun NavGraphBuilder.chatGraph(
    navigationAction: MainNavigationAction,
    navigateToLogin:() -> Unit
) {
    composable(
        route = "${MainDestinations.CHAT_ROUTE}/{${DestinationID.CHAT_ID}}",
        deepLinks = listOf(navDeepLink { uriPattern = "https://plopmessenger/{${DestinationID.CHAT_ID}}" })
    ) { from ->
        ChatScreen(
            upPress = navigationAction.upPress,
            navigateToChatInfo = { chatId -> navigationAction.navigateToChatInfo(chatId, from)},
            navigateToAddMember = { chatId -> navigationAction.navigateToAddMember(chatId, from)}
        )
    }

    composable(
        route = "${MainDestinations.CHAT_ROUTE}/{${DestinationID.CHAT_ID}}/{${DestinationID.PEOPLE_LIST}}",
        arguments = listOf(
            navArgument(DestinationID.PEOPLE_LIST) { type = PeopleParcelableModel }
        )
    ) { from ->

        val arguments = requireNotNull(from.arguments)
        val peopleList = arguments.getParcelable<PeopleParcelableModel>(DestinationID.PEOPLE_LIST)
        ChatScreen(
            upPress = navigationAction.upPress,
            navigateToChatInfo = { chatId -> navigationAction.navigateToChatInfo(chatId, from)},
            navigateToAddMember = { chatId -> navigationAction.navigateToAddMember(chatId, from)},
            peopleList = peopleList?.peopleList
        )
    }

    composable(
        route = "${MainDestinations.NEW_CHAT_ROUTE}/{${DestinationID.PEOPLE_LIST}}",
        arguments = listOf(
            navArgument(DestinationID.PEOPLE_LIST) { type = PeopleParcelableModel }
        )
    ) { from ->
        val arguments = requireNotNull(from.arguments)
        val peopleList = arguments.getParcelable<PeopleParcelableModel>(DestinationID.PEOPLE_LIST)

        ChatScreen(
            upPress = navigationAction.upPress,
            navigateToChatInfo = { chatId -> navigationAction.navigateToChatInfo(chatId, from)},
            navigateToAddMember = { chatId -> navigationAction.navigateToAddMember(chatId, from)},
            peopleList = peopleList?.peopleList
        )
    }

    composable(
        route = "${MainDestinations.CHAT_INFO_ROUTE}/{${DestinationID.CHAT_ID}}"
    ) { from ->
        ChatInfoScreen(
            navigateToAddMember = { chatId -> navigationAction.navigateToAddMember(chatId, from)},
            navigateToChats = navigationAction.navigateLeaveChat,
            upPress = navigationAction.upPress
        )
    }

    composable(
        route = "${MainDestinations.ADD_CHAT_MEMBER_ROUTE}/{${DestinationID.CHAT_ID}}"
    ) { from ->
        AddChatMemberScreen(
            upPress = navigationAction.upPress,
            navigateToNewChat = navigationAction.navigateToNewChat,
            navigateToUpdateGroupChat = navigationAction.navigateToUpdateGroupChat
        )
    }

    composable(
        route = MainDestinations.ADD_CHAT_ROUTE
    ) { from ->
        AddChatScreen(
            upPress = navigationAction.upPress,
            navigateToNewChat = navigationAction.navigateToNewChat,
            navigateToAddGroupChat = navigationAction.navigateToAddGroupChat
        )
    }

    composable(
        route = MainDestinations.ADD_CHAT_GROUP_ROUTE
    ) { from ->
        AddGroupChatScreen(
            upPress = navigationAction.upPress,
            navigateToNewChat = navigationAction.navigateToNewChat
        )
    }
}

private fun NavGraphBuilder.peopleGraph(
    navigationAction: MainNavigationAction,
    navigateToLogin:() -> Unit
) {
    composable(BottomBarDestinations.PEOPLE_ROUTE) { from ->
        PeopleScreen(
            navigateToAddPeople = navigationAction.navigateToAddPeople
        )
    }
    composable(MainDestinations.ADD_PEOPLE_ROUTE) { from ->
        AddPeopleScreen(
            upPress = navigationAction.upPress
        )
    }
}

private fun NavGraphBuilder.settingGraph(
    navigationAction: MainNavigationAction,
    navigateToLogin:() -> Unit
) {
    composable(BottomBarDestinations.SETTING_ROUTE) { from ->
        SettingScreen(navigationAction.navigateToModifyProfile, navigateToLogin)
    }

    composable(MainDestinations.MODIFY_PROFILE) { from ->
        ModifyProfileScreen(upPress = navigationAction.upPress)
    }
}