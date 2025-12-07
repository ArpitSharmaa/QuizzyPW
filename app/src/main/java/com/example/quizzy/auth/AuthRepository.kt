package com.example.quizzy.auth

import com.example.quizzy.page_state.AuthState

interface AuthRepository{
    suspend fun logIn(email : String, password : String) : AuthState
    suspend fun signUp(email: String, password: String) : AuthState
    suspend fun forgotPassword(email: String) : AuthState
    suspend fun logOut() : AuthState
    suspend fun isLoggedIn() : AuthState
}