package com.project.standard_login.di

import com.google.firebase.auth.FirebaseAuth
import com.project.standard_login.data.repository.LoginRepositoryImpl
import com.project.standard_login.domain.repository.LoginRepository
import com.project.standard_login.security.SecureStorage
import com.project.standard_login.ui.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { FirebaseAuth.getInstance() }
    
    // Security / Data Sources
    single { SecureStorage(androidContext()) }
    
    // Repositories
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    
    // ViewModel
    viewModel { LoginViewModel(get(), get()) }
}
