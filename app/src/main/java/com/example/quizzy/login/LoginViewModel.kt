package com.example.quizzy.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.auth.AuthRepository
import com.example.quizzy.page_state.AuthState
import com.example.quizzy.util.CharLimits
import com.example.quizzy.util.Utility.coroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")

    var emailError by mutableStateOf(false)

    fun setEmailText(emailType: String) {
        email = if (emailType.length <= CharLimits.EMAIL_LIMIT_MAX) emailType else emailType.take(
            CharLimits.EMAIL_LIMIT_MAX
        )
        emailError = email.length < CharLimits.EMAIL_LIMIT_MIN
    }

    var password by mutableStateOf("")
    var passwordError by mutableStateOf(false)

    fun setPasswordText(passwordType: String) {
        password = if (passwordType.length <= CharLimits.PASSWORD_LIMIT_MAX) passwordType else passwordType.take(
            CharLimits.PASSWORD_LIMIT_MAX
        )
        passwordError = password.length < CharLimits.PASSWORD_LIMIT_MIN
    }

    val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState = _loginState.asStateFlow()

    fun loginUser() {
        _loginState.update { AuthState.Loading }
        val job = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _loginState.emit(authRepository.logIn(email,password))
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                _loginState.update { AuthState.ErrorOverAuth(Exception(cause.message ?: "Unknown Error")) }
            }
        }
    }

    fun logoutUser() {
        _loginState.update { AuthState.Loading }
        val job = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _loginState.emit(authRepository.logOut())
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                _loginState.update { AuthState.ErrorOverAuth(Exception(cause.message ?: "Unknown Error")) }
            }
        }
    }

    fun signUpUser() {
        _loginState.update { AuthState.Loading }
        val job = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _loginState.emit(authRepository.signUp(email,password))
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                _loginState.update { AuthState.ErrorOverAuth(Exception(cause.message ?: "Unknown Error")) }
            }
        }
    }

    fun forgotPassWord() {
        _loginState.update { AuthState.Loading }
        val job = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _loginState.emit(authRepository.forgotPassword(email))
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                _loginState.update { AuthState.ErrorOverAuth(Exception(cause.message ?: "Unknown Error")) }
            }
        }
    }
}