package com.project.standard_login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.standard_login.data.LoginRepository
import com.project.standard_login.data.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                _loginState.value = result
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _loginState.value = LoginResult.Error("Por favor, insira o email.")
            return
        }
        viewModelScope.launch {
            repository.resetPassword(email).collect { result ->
                _loginState.value = result
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            repository.loginWithGoogle(idToken).collect { result ->
                _loginState.value = result
            }
        }
    }
    
    fun clearState() {
        _loginState.value = LoginResult.Idle
    }
}
