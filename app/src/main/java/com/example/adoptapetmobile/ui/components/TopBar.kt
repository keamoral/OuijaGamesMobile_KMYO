package com.example.adoptapetmobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    titulo: String,
    mostrarAtras: Boolean = false,
    onClickAtras: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = titulo)
        },
        navigationIcon = {
            if (mostrarAtras) {
                IconButton(onClick = onClickAtras) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}