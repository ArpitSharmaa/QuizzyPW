package com.example.quizzy.di

import com.example.quizzy.auth.AuthRepository
import com.example.quizzy.auth.AuthRepositoryImp
import com.example.quizzy.home.repository.HomeRepository
import com.example.quizzy.home.repository.HomeRepositoryImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepositoryImp) { bind<AuthRepository>() }
    singleOf(::HomeRepositoryImp) { bind<HomeRepository>() }
}