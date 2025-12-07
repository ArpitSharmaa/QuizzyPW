package com.example.quizzy

import android.app.Application
import com.example.quizzy.di.networkModule
import com.example.quizzy.di.repositoryModule
import com.example.quizzy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class QuizzyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@QuizzyApplication)
            modules(
                listOf(
                    repositoryModule,
                    viewModelModule,
                    networkModule
                )
            )
        }
    }
}