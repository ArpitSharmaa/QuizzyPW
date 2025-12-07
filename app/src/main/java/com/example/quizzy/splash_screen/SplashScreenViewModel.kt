package com.example.quizzy.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.auth.AuthRepository
import com.example.quizzy.page_state.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _splashScreenState = MutableStateFlow<AuthState>(AuthState.Loading)
    val splashScreenState = _splashScreenState.asStateFlow()

    fun checkIfUserLoggedIn() {
        val job = viewModelScope.launch {
            _splashScreenState.emit(authRepository.isLoggedIn())
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                _splashScreenState.update { AuthState.ErrorOverAuth(Exception(cause.message ?: "Unknown Error")) }
            }
        }
    }
}