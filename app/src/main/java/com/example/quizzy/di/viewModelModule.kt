package com.example.quizzy.di

import com.example.quizzy.home.HomeScreenViewModel
import com.example.quizzy.login.LoginViewModel
import com.example.quizzy.splash_screen.SplashScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeScreenViewModel)
}