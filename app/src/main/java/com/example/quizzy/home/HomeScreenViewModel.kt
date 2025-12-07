package com.example.quizzy.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.home.repository.HomeRepository
import com.example.quizzy.model_class.DashboardResponse
import com.example.quizzy.page_state.HomeState
import com.example.quizzy.util.Utility.coroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _dashboardData = MutableStateFlow<HomeState>(HomeState.Idle)
    val dashboardResponse = _dashboardData.asStateFlow()


    fun getDashboardData() {
        _dashboardData.update { HomeState.Loading }
        val job = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _dashboardData.emit(homeRepository.getHomeData())
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                _dashboardData.update { HomeState.Error(cause.message ?: "Unknown Error") }
            }
        }
    }

    fun setDashData(dashboardResponse: DashboardResponse?) {
        dashboardResponse?.let { data ->
            _dashboardData.update { HomeState.OnSuccess(data) }
        } ?: run {
            getDashboardData()
        }
    }
}