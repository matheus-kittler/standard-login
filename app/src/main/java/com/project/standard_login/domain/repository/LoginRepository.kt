package com.project.standard_login.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.project.standard_login.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun login(email: String, password: String): Flow<LoginResult>
    fun resetPassword(email: String): Flow<LoginResult>
    fun loginWithGoogle(idToken: String): Flow<LoginResult>
    fun getCurrentUser(): FirebaseUser?
}
