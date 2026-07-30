package com.project.standard_login.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.standard_login.domain.repository.LoginRepository
import com.project.standard_login.domain.model.LoginResult
import com.project.standard_login.data.local.SecureStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val secureStorage: SecureStorage
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState.asStateFlow()

    private val _savedEmail = MutableStateFlow<String?>(null)
    val savedEmail: StateFlow<String?> = _savedEmail.asStateFlow()

    init {
        _savedEmail.value = secureStorage.getEmail()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                _loginState.value = result
                if (result is LoginResult.Success) {
                    secureStorage.saveEmail(email)
                    _savedEmail.value = email
                }
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
                if (result is LoginResult.Success) {
                    // No caso do Google, o email vem do resultado
                    val userEmail = (result as LoginResult.Success).user
                    secureStorage.saveEmail(userEmail)
                    _savedEmail.value = userEmail
                }
            }
        }
    }
    
    fun logout() {
        secureStorage.clear()
        _savedEmail.value = null
        _loginState.value = LoginResult.Idle
    }

    fun clearState() {
        _loginState.value = LoginResult.Idle
    }
}
