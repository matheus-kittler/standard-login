package com.project.standard_login

import android.app.Application
import com.project.standard_login.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Habilita o log do Koin para facilitar o debug
            androidLogger()
            // Configura o contexto do Android
            androidContext(this@MainApplication)
            // Carrega os módulos
            modules(appModule)
        }
    }
}
