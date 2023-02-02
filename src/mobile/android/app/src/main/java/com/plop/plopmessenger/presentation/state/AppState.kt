package com.plop.plopmessenger.presentation.state

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.plop.plopmessenger.presentation.component.BottomBarTabs
import com.plop.plopmessenger.presentation.navigation.MainNavigationAction


@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController()
) = remember(scaffoldState, navController) {
    AppState(scaffoldState, navController)
}


class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController
) {
    val bottomBarTabs = BottomBarTabs.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val currentRoute: String?
        get() = navController.currentDestination?.route

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val navigationAction = MainNavigationAction(
        navController
    )
}