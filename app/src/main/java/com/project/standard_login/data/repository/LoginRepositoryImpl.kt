package com.project.standard_login.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.project.standard_login.domain.model.LoginResult
import com.project.standard_login.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(private val auth: FirebaseAuth) : LoginRepository {
    
    override fun login(email: String, password: String): Flow<LoginResult> = flow {
        emit(LoginResult.Loading)
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(LoginResult.Success(result.user?.email ?: "Usuário logado"))
        } catch (e: Exception) {
            emit(LoginResult.Error(e.message ?: "Erro ao fazer login"))
        }
    }

    override fun resetPassword(email: String): Flow<LoginResult> = flow {
        emit(LoginResult.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(LoginResult.Success("Email de redefinição enviado com sucesso"))
        } catch (e: Exception) {
            emit(LoginResult.Error(e.message ?: "Erro ao enviar email de redefinição"))
        }
    }

    override fun loginWithGoogle(idToken: String): Flow<LoginResult> = flow {
        emit(LoginResult.Loading)
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            emit(LoginResult.Success(result.user?.email ?: "Login com Google realizado"))
        } catch (e: Exception) {
            emit(LoginResult.Error(e.message ?: "Erro no login com Google"))
        }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
