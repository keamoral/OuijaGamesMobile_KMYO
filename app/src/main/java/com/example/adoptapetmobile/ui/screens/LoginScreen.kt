package com.example.adoptapetmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun LoginScreen(viewModel: AuthViewModel = viewModel(), onLoggedIn: () -> Unit, onGoRegister: () -> Unit){
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("OuijaGames", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo")})
        OutlinedTextField(
            value = password,
            onValueChange = { password = it},
            label = { Text("Clave") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            if(correo.isBlank() || password.isBlank()) {
                mensaje = "Por favor, completa todos los campos."
            }else{
                viewModel.iniciarSesion(correo, password) { exito, error ->
                    mensaje = if (exito) {
                        onLoggedIn()
                        "Inicio de sesion exitoso!"
                    } else {
                        when {
                            error?.contains("no user record", true) == true ->
                                "El usuario no esta registrado."
                            error?.contains("password is invalid", true) == true ->
                                "La clave ingresada es incorrecta."
                            else ->
                                "Error al iniciar sesion, Revisa tus datos."
                        }
                    }
                }
            }
        }){
            Text("Iniciar Sesion")
        }

        if (mensaje.isNotEmpty()){
            val colorTexto = when {
                mensaje.contains("incorrrecta", true) ||
                        mensaje.contains("Error", true)||
                        mensaje.contains("Por favor", true)||
                        mensaje.contains("no esta", true) -> Color.Red
                mensaje.contains("exitoso", true) -> Color(0xFF1B5E20)
                else-> Color.Red
            }
            Text(mensaje, color = colorTexto, modifier = Modifier.padding(top = 10.dp))

        }


        Divider(Modifier.padding(vertical = 8.dp))
        OutlinedButton(onClick = onGoRegister) { Text("Registrarse") }
    }

}