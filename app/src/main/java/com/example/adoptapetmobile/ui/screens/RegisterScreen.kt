package com.example.adoptapetmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adoptapetmobile.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegistered: () -> Unit = {},
    onGoLogin: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var timeoutReached by remember { mutableStateOf(false) }

    // Timeout de 15 segundos
    LaunchedEffect(isLoading) {
        if (isLoading) {
            timeoutReached = false
            delay(15000) // 15 segundos
            if (isLoading) {
                timeoutReached = true
                isLoading = false
                mensaje = "La conexión está tardando demasiado. Verifica tu internet e intenta nuevamente."
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("OuijaGames", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            enabled = !isLoading
        )
        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text("RUT") },
            enabled = !isLoading
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            enabled = !isLoading
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Clave") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (usuario.isBlank() || rut.isBlank() || correo.isBlank() || password.isBlank()) {
                    mensaje = "Por favor, completa todos los campos"
                } else {
                    isLoading = true
                    mensaje = ""
                    timeoutReached = false

                    viewModel.registrar(usuario, rut, correo, password) { exito, error ->
                        if (!timeoutReached) {
                            isLoading = false
                            if (exito) {
                                mensaje = ""
                                showSuccessDialog = true
                            } else {
                                mensaje = when {
                                    error?.contains("already in use", true) == true ->
                                        "El correo ingresado ya esta registrado."
                                    error?.contains("badly formatted", true) == true ->
                                        "El formato del correo no es valido."
                                    error?.contains("weak-password", true) == true ||
                                            error?.contains("WEAK_PASSWORD", true) == true ->
                                        "La clave es demasiado debil. Usa al menos 6 caracteres."
                                    error?.contains("network", true) == true ->
                                        "Error de conexión. Verifica tu internet."
                                    else -> "Error: ${error ?: "Desconocido"}. Intenta nuevamente."
                                }
                            }
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.height(50.dp)
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Registrando...")
                }
            } else {
                Text("Registrarse")
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = mensaje,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(Modifier.height(8.dp))
        Divider(Modifier.padding(vertical = 8.dp))

        OutlinedButton(
            onClick = onGoLogin,
            enabled = !isLoading
        ) {
            Text("Iniciar Sesion")
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "¡Registro exitoso!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF1B5E20)
                )
            },
            text = {
                Text(
                    text = "Tu cuenta ha sido creada correctamente.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onGoLogin()
                    }
                ) {
                    Text("Ir a Iniciar Sesión")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}