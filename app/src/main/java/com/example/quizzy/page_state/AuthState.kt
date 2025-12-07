package com.example.quizzy.page_state

import com.example.quizzy.model_class.DashboardResponse

sealed class AuthState() {
    object Idle : AuthState()
    object Loading : AuthState()
    data class SuccessOverAuth(val message: String) : AuthState()

    data class SuccessOverDashBoard(val dashboardResponse: DashboardResponse?) : AuthState() {
        companion object {
            const val DASHBOARD_ARGS = "DASHBOARD_ARGS"
        }
    }

    data class ErrorOverAuth(val error: Exception?) : AuthState()
}
