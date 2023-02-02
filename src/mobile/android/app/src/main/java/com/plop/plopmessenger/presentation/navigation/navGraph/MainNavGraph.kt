package com.plop.plopmessenger.presentation.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.plop.plopmessenger.presentation.navigation.BottomBarDestinations
import com.plop.plopmessenger.presentation.navigation.MainDestinations
import com.plop.plopmessenger.presentation.navigation.MainNavigationAction
import com.plop.plopmessenger.presentation.screen.main.Chats
import com.plop.plopmessenger.presentation.screen.main.People
import com.plop.plopmessenger.presentation.screen.main.Setting

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
        }
    }
}

private fun NavGraphBuilder.bottomNavGraph(
    navigationAction: MainNavigationAction
) {
    composable(BottomBarDestinations.CHATS_ROUTE) { from ->
        Chats()
    }

    composable(BottomBarDestinations.PEOPLE_ROUTE) { from ->
        People()
    }

    composable(BottomBarDestinations.SETTING_ROUTE) { from ->
        Setting()
    }

}
