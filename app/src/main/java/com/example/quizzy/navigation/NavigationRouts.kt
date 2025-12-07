package com.example.quizzy.navigation

sealed class NavigationRouts (val routs: String) {
    // Graphs
    object SplashGraph : NavigationRouts("splash_graph")
    object AuthGraph : NavigationRouts("auth_graph")
    object HomeGraph : NavigationRouts("home_graph")

    // Splash Screens
    object Splash : NavigationRouts("splash_screen")

    // Auth Screens
    object Login : NavigationRouts("login_page")
    object SignUp : NavigationRouts("signup_page")
    object ForgotPassword : NavigationRouts("forgot_password_page")

    // Home Screens
    object Home : NavigationRouts("home_page")
    object NotificationScreen : NavigationRouts("notification_page")
}