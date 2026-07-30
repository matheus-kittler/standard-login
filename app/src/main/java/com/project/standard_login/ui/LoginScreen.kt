package com.project.standard_login.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.project.standard_login.data.LoginResult
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Google Sign-In Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            // Tratar erro
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        
        TextButton(
            onClick = { viewModel.resetPassword(email) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Esqueci minha senha")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is LoginResult.Loading
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("SEU_WEB_CLIENT_ID_AQUI") // Substituir pelo ID do Firebase
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is LoginResult.Loading
        ) {
            Text("Entrar com Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is LoginResult.Loading -> CircularProgressIndicator()
            is LoginResult.Success -> {
                Text(
                    text = "Sucesso: ${(state as LoginResult.Success).user}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            is LoginResult.Error -> {
                Text(
                    text = "Erro: ${(state as LoginResult.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {}
        }
    }
}
