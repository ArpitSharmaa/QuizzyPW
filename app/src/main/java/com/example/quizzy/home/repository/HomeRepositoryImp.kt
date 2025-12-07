package com.example.quizzy.home.repository

import com.example.quizzy.page_state.HomeState
import com.example.quizzy.retrofit.ApiService

class HomeRepositoryImp(val apiService: ApiService) : HomeRepository {
    override suspend fun getHomeData(): HomeState {
        val result = apiService.getDashBoardData()
        return if (result.isSuccessful && result.body() != null) {
            HomeState.OnSuccess(
                result.body()!!
            )
        } else {
            HomeState.Error(
                result.message()
            )
        }
    }
}