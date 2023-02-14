package com.plop.plopmessenger.presentation.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plop.plopmessenger.presentation.navigation.LoginDestinations
import com.plop.plopmessenger.presentation.navigation.LoginNavigationAction
import com.plop.plopmessenger.presentation.screen.login.FindPasswordScreen
import com.plop.plopmessenger.presentation.screen.login.LoginScreen
import com.plop.plopmessenger.presentation.screen.login.SignUpScreen


@Composable
fun LoginNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = LoginDestinations.LOGIN_ROUTE,
    navigationAction: LoginNavigationAction,
    navigateToMain: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(LoginDestinations.LOGIN_ROUTE) {
            LoginScreen(
                navigateToSignUp = navigationAction.navigateToSignUp,
                navigateToMain = navigateToMain,
                navigateToFindPassword = navigationAction.navigateToFindPassword
            )
        }

        composable(LoginDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(navigationAction.upPress)
        }

        composable(LoginDestinations.FIND_PASSWORD_ROUTE) {
            FindPasswordScreen(navigationAction.upPress)
        }
    }
}