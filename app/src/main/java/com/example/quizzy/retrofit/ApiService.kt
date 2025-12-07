package com.example.quizzy.retrofit

import com.example.quizzy.model_class.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(EndPoints.ENDPOINT_FOR_HOME_DATA)
    suspend fun getDashBoardData(): Response<DashboardResponse>
}