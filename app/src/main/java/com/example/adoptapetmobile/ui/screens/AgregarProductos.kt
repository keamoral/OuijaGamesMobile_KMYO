package com.example.adoptapetmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.adoptapetmobile.ui.components.TopBar
import com.example.adoptapetmobile.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProducto(
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val creationSuccess by viewModel.creationSuccess.collectAsState()
    val categorias by viewModel.categorias.collectAsState()

    var expandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                titulo = "Agregar Producto",
                mostrarAtras = true,
                onClickAtras = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre del producto
            OutlinedTextField(
                value = formState.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre del producto *") },
                isError = formState.nameError != null,
                supportingText = {
                    formState.nameError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            OutlinedTextField(
                value = formState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Descripción *") },
                isError = formState.descriptionError != null,
                supportingText = {
                    formState.descriptionError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Precio
            OutlinedTextField(
                value = formState.price,
                onValueChange = { viewModel.onPriceChange(it) },
                label = { Text("Precio (CLP) *") },
                isError = formState.priceError != null,
                supportingText = {
                    formState.priceError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stock
            OutlinedTextField(
                value = formState.stock,
                onValueChange = { viewModel.onStockChange(it) },
                label = { Text("Stock *") },
                isError = formState.stockError != null,
                supportingText = {
                    formState.stockError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // URL de imagen
            OutlinedTextField(
                value = formState.img,
                onValueChange = { viewModel.onImgChange(it) },
                label = { Text("URL de la imagen *") },
                isError = formState.imgError != null,
                supportingText = {
                    formState.imgError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de categoría DINÁMICO
            if (categorias.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido }
                ) {
                    OutlinedTextField(
                        value = categorias.find { it.id.toString() == formState.categoriaId }?.nombre
                            ?: "Seleccionar categoría",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false }
                    ) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria.nombre) },
                                onClick = {
                                    viewModel.onCategoriaIdChange(categoria.id.toString())
                                    expandido = false
                                }
                            )
                        }
                    }
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text("Cargando categorías...", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar error general si existe
            error?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botón guardar
            Button(
                onClick = { viewModel.createProduct() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && categorias.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardando...")
                } else {
                    Text("Guardar Producto")
                }
            }
        }
    }

    // Diálogo de éxito
    if (creationSuccess) {
        AlertDialog(
            onDismissRequest = { viewModel.resetCreationSuccess() },
            title = { Text("¡Éxito!") },
            text = { Text("El producto fue agregado correctamente a la tienda") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetCreationSuccess()
                        navController.popBackStack()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}