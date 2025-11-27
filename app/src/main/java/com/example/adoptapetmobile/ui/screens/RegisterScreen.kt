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
fun RegisterScreen(viewModel: AuthViewModel = viewModel(), onRegistered: () -> Unit = {}, onGoLogin: () -> Unit){
    var usuario by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var registroExitoso by remember { mutableStateOf(false) }

    if (registroExitoso){

    }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("OuijaGames", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(value = usuario, onValueChange = { usuario = it }, label = {Text("Usuario")})
        OutlinedTextField(value = rut, onValueChange = { rut = it }, label = {Text("RUT")})
        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = {Text("Correo")})
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Clave") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            if(usuario.isBlank()||rut.isBlank()||correo.isBlank()||password.isBlank()){
                mensaje = "Por favor, completa todos los campos"
            }else{
                viewModel.registrar(usuario, rut, correo, password) { exito, error ->
                    mensaje = if (exito) {
                        onGoLogin()
                        "Registro exitoso!"


                    } else {
                        when{
                            error?.contains("already in use", true) == true ->
                                "El correo ingresado ya esta registrado."
                            error?.contains("badly formatted", true) == true ->
                                "El formato del correo no es valido."
                            error?.contains("weak-password", true) == true||
                                    error?.contains("WEAK_PASSWORD", true) == true ->
                                        "La clave es demasiado debil. Usa al menos 6 caracteres."
                            else -> "Error al registrar usuario. Intente nuevamente."
                        }
                    }

                }
            }

        }) {
            Text("Registrarse")
        }

        if(mensaje.isNotEmpty()){
            val colorTexto = when {
                mensaje.contains("Error", true)||
                        mensaje.contains("Por favor", true)||
                        mensaje.contains("registrado", true)||
                        mensaje.contains("valido", true)||
                        mensaje.contains("debil", true) -> Color.Red
                mensaje.contains("exitoso", true) -> Color(0xFF1B5E20)
                else -> Color.Red
            }
            Text(mensaje, color = colorTexto, modifier = Modifier.padding(top = 10.dp))
        }


        Divider(Modifier.padding(vertical = 8.dp))
        OutlinedButton(onClick = onGoLogin) { Text("Iniciar Sesion") }

    }
}