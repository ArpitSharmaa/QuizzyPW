package com.example.quizzy.home.repository

import com.example.quizzy.page_state.HomeState

interface HomeRepository {
    suspend fun getHomeData() : HomeState
}