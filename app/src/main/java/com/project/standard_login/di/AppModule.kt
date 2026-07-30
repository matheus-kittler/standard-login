package com.project.standard_login.di

import com.google.firebase.auth.FirebaseAuth
import com.project.standard_login.data.LoginRepository
import com.project.standard_login.data.LoginRepositoryImpl
import com.project.standard_login.ui.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    viewModel { LoginViewModel(get()) }
}
