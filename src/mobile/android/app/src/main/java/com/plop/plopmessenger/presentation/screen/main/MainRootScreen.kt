package com.plop.plopmessenger.presentation.screen.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.plop.plopmessenger.presentation.component.PlopBottomBar
import com.plop.plopmessenger.presentation.navigation.MainDestinations
import com.plop.plopmessenger.presentation.navigation.navGraph.MainNavGraph
import com.plop.plopmessenger.presentation.state.rememberAppState
import com.plop.plopmessenger.presentation.theme.PlopMessengerTheme

@Composable
fun MainRootScreen() {
    PlopMessengerTheme() {
        val appState = rememberAppState()
        val navigationAction = appState.navigationAction

        Scaffold(
            scaffoldState = appState.scaffoldState,
            bottomBar = {
                if(appState.shouldShowBottomBar) {
                    PlopBottomBar(
                        tabs = appState.bottomBarTabs,
                        currentRoute = appState.currentRoute,
                        navigateToRoute = navigationAction.navigateToBottomRoute
                    )
                }
            }
        ) { innerPadding ->
            MainNavGraph(
                navController = appState.navController,
                startDestination = MainDestinations.MAIN_ROUTE,
                navigationAction = navigationAction
            )
        }

    }
}