package com.plop.plopmessenger.presentation.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.plop.plopmessenger.presentation.navigation.BottomBarDestinations
import com.plop.plopmessenger.presentation.navigation.DestinationID
import com.plop.plopmessenger.presentation.navigation.MainDestinations
import com.plop.plopmessenger.presentation.navigation.MainNavigationAction
import com.plop.plopmessenger.presentation.screen.main.*

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.MAIN_ROUTE,
    navigationAction: MainNavigationAction
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            route = MainDestinations.MAIN_ROUTE,
            startDestination = BottomBarDestinations.CHATS_ROUTE
        ) {
            bottomNavGraph(navigationAction)
            chatGraph(navigationAction)
        }
    }
}

private fun NavGraphBuilder.bottomNavGraph(
    navigationAction: MainNavigationAction
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
        peopleGraph(navigationAction)
    }

    composable(BottomBarDestinations.SETTING_ROUTE) { from ->
        SettingScreen()
    }

}

private fun NavGraphBuilder.chatGraph(
    navigationAction: MainNavigationAction
) {
    composable(
        route = MainDestinations.CHAT_ROUTE
    ) { from ->
        ChatScreen(
            navigateToChatInfo = { chatId -> navigationAction.navigateToChatInfo(chatId, from)}
        )
    }

    composable(
        route = "${MainDestinations.CHAT_ROUTE}/{${DestinationID.CHAT_ID}}"
    ) { from ->
        ChatScreen(
            navigateToChatInfo = { chatId -> navigationAction.navigateToChatInfo(chatId, from)}
        )
    }

    composable(
        route = "${MainDestinations.CHAT_INFO_ROUTE}/{${DestinationID.CHAT_ID}}"
    ) { from ->
        ChatInfoScreen(
            navigateToAddMember = { chatId -> navigationAction.navigateToAddMember(chatId, from)},
            upPress = navigationAction.upPress
        )
    }

    composable(
        route = "${MainDestinations.ADD_CHAT_MEMBER_ROUTE}/{${DestinationID.CHAT_ID}}"
    ) { from ->
        AddChatMemberScreen(
            upPress = navigationAction.upPress,
            navigateToNewChat = { navigationAction.navigateToNewChat() },
            navigateToUpdateGroupChat = { chatId -> navigationAction.navigateToUpdateGroupChat(chatId, from)}
        )
    }

    composable(
        route = MainDestinations.ADD_CHAT_ROUTE
    ) { from ->
        AddChatScreen(
            upPress = navigationAction.upPress,
            navigateToNewChat = { navigationAction.navigateToNewChat() },
            navigateToAddGroupChat = { navigationAction.navigateToAddGroupChat() }
        )
    }

    composable(
        route = MainDestinations.ADD_CHAT_GROUP_ROUTE
    ) { from ->
        AddGroupChatScreen(
            upPress = navigationAction.upPress,
            navigateToNewChat = { navigationAction.navigateToNewChat() },
        )
    }
}

private fun NavGraphBuilder.peopleGraph(
    navigationAction: MainNavigationAction
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