package com.project.standard_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.standard_login.ui.LoginScreen
import com.project.standard_login.ui.theme.LoginAppTheme

/**
 * Main Activity que hospeda a tela de login e permite alternar entre modo claro e escuro.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Estado para controlar se o tema é escuro ou claro (inicia com o padrão do sistema)
            val systemInDarkTheme = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemInDarkTheme) }

            LoginAppTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Standard Login", style = MaterialTheme.typography.titleLarge) },
                            actions = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 16.dp)
                                ) {
                                    Text("Dark Mode", style = MaterialTheme.typography.labelSmall)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Switch(
                                        checked = isDarkMode,
                                        onCheckedChange = { isDarkMode = it }
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // A Surface garante que o fundo mude de cor automaticamente
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        LoginScreen()
                    }
                }
            }
        }
    }
}
