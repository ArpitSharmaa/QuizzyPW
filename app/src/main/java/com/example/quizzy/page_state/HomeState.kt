package com.example.quizzy.page_state

import com.example.quizzy.model_class.DashboardResponse

sealed class HomeState {
    object Loading : HomeState()
    data class Error(val message: String) : HomeState()
    data class OnSuccess(val data: DashboardResponse) : HomeState()
    object Idle : HomeState()
}