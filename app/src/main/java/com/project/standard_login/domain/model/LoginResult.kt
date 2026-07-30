package com.project.standard_login.domain.model

sealed class LoginResult {
    data class Success(val user: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
    object Idle : LoginResult()
}
