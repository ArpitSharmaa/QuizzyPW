package com.example.quizzy.di

import com.example.quizzy.BuildConfig
import com.example.quizzy.model_class.PerformanceTrend
import com.example.quizzy.model_class.StreakStatus
import com.example.quizzy.retrofit.ApiService
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    singleOf(::provideFirebaseAuth)
    singleOf(::provideOkHttpClient)
    singleOf(::provideRetrofit)
    singleOf(::provideApiService)
}

fun provideFirebaseAuth() = Firebase.auth

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .serializeNulls()
                    .registerTypeAdapter(
                        StreakStatus::class.java, StreakStatusAdapter()
                    )
                    .registerTypeAdapter(
                        PerformanceTrend::class.java, PerformanceTrendAdapter()
                    )
                    .create()
            )
        )
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


class StreakStatusAdapter : TypeAdapter<StreakStatus?>() {
    override fun write(
        out: JsonWriter?,
        value: StreakStatus?
    ) {
        out?.value(value?.value)
    }

    override fun read(`in`: JsonReader?): StreakStatus? {
        return if (`in` == null) {
            null
        } else if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            null
        } else {
            StreakStatus.fromValue(`in`.nextString())
        }
    }

}

class PerformanceTrendAdapter : TypeAdapter<PerformanceTrend?>() {
    override fun write(
        out: JsonWriter?,
        value: PerformanceTrend?
    ) {
        out?.value(value?.value)
    }

    override fun read(`in`: JsonReader?): PerformanceTrend? {
        return if (`in` == null) {
            null
        } else if (`in`.peek() == JsonToken.NULL) {
            `in`.nextNull()
            null
        } else {
            PerformanceTrend.fromValue(`in`.nextString())
        }
    }

}