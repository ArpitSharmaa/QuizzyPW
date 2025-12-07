package com.example.quizzy.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quizzy.page_state.AuthState
import com.example.quizzy.navigation.NavigationRouts
import com.example.quizzy.util.SetStatusBarColor
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashScreenViewModel = koinViewModel()
) {
    val state by viewModel.splashScreenState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkIfUserLoggedIn()
    }
    LaunchedEffect(state) {
        when (state) {
            is AuthState.Loading -> Unit
            is AuthState.ErrorOverAuth -> {
                delay(1000)
                navHostController.navigate(NavigationRouts.AuthGraph.routs) {
                    popUpTo(NavigationRouts.SplashGraph.routs) { inclusive = true }
                }
            }

            is AuthState.SuccessOverAuth -> {
                delay(1000)
                navHostController.navigate(NavigationRouts.HomeGraph.routs) {
                    popUpTo(NavigationRouts.SplashGraph.routs) { inclusive = true }
                }
            }

            is AuthState.Idle -> Unit
            is AuthState.SuccessOverDashBoard -> Unit
        }
    }
    SetStatusBarColor(useDarkIcons = false)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("Quizzy", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 32.sp)
    }
}