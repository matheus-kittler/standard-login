package com.project.standard_login.di

import com.google.firebase.auth.FirebaseAuth
import com.project.standard_login.data.local.SecureStorage
import com.project.standard_login.data.repository.LoginRepositoryImpl
import com.project.standard_login.domain.repository.LoginRepository
import com.project.standard_login.presentation.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { FirebaseAuth.getInstance() }
    
    // Local Data Source (Keystore)
    single { SecureStorage(androidContext()) }
    
    // Repositories (Data Layer)
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    
    // ViewModels (Presentation Layer)
    viewModel { LoginViewModel(get(), get()) }
}
