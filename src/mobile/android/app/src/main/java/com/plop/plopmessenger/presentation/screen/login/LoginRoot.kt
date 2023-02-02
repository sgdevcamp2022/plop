package com.plop.plopmessenger.presentation.screen.login

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.plop.plopmessenger.presentation.navigation.LoginNavigationAction
import com.plop.plopmessenger.presentation.navigation.navGraph.LoginNavGraph
import com.plop.plopmessenger.presentation.theme.PlopMessengerTheme


@Composable
fun LoginRoot(navigateToMain:() -> Unit) {
    PlopMessengerTheme() {
        val scaffoldState = rememberScaffoldState()
        val navController = rememberNavController()
        val navigationAction = LoginNavigationAction(navController)

        Scaffold(
            scaffoldState = scaffoldState
        ) {
            LoginNavGraph(
                navController = navController,
                navigationAction = navigationAction,
                navigateToMain = navigateToMain
            )
        }
    }
}