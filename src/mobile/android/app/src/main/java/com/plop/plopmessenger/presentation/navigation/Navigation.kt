package com.plop.plopmessenger.presentation.navigation

import androidx.navigation.NavController

object LoginDestinations {
    const val LOGIN_ROUTE = "login"
    const val SIGN_UP_ROUTE = "signup"
}

class LoginNavigationAction(navController: NavController) {
    val upPress:() -> Unit = {
        navController.navigateUp()
    }

    val navigateToSignUp:() -> Unit = {
        navController.navigate(LoginDestinations.SIGN_UP_ROUTE)
    }
}