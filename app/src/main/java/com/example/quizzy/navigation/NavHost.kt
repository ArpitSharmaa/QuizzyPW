package com.example.quizzy.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.quizzy.forgot_password.ForgotPasswordPage
import com.example.quizzy.home.HomePage
import com.example.quizzy.login.LoginPage
import com.example.quizzy.notification.NotificationsSettingsScreen
import com.example.quizzy.signup.SignupPage
import com.example.quizzy.splash_screen.SplashScreen


@Composable
fun Navigation(modifier: Modifier, paddingValues: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationRouts.SplashGraph.routs
    ) {

        navigation(   //Splash Screen Graph
            startDestination = NavigationRouts.Splash.routs,
            route = NavigationRouts.SplashGraph.routs
        ) {

            composable(NavigationRouts.Splash.routs) {
                SplashScreen(
                    navController
                )
            }
        }

        navigation(  //Auth Screen Graph
            startDestination = NavigationRouts.Login.routs,
            route = NavigationRouts.AuthGraph.routs
        ) {

            composable(NavigationRouts.Login.routs) {
                LoginPage(
                    navController,paddingValues = paddingValues
                )
            }

            composable(NavigationRouts.SignUp.routs) {
                SignupPage(navController, paddingValues = paddingValues)
            }

            composable(NavigationRouts.ForgotPassword.routs) {
                ForgotPasswordPage(navController, paddingValues = paddingValues)
            }
        }

        navigation(     //Home Screen Graph
            startDestination = NavigationRouts.Home.routs,
            route = NavigationRouts.HomeGraph.routs
        ) {
            composable(NavigationRouts.Home.routs) {
                HomePage(navController, paddingValues = paddingValues)
            }

            composable(NavigationRouts.NotificationScreen.routs) {
                NotificationsSettingsScreen(navController, paddingValues = paddingValues)
            }
        }
    }

}