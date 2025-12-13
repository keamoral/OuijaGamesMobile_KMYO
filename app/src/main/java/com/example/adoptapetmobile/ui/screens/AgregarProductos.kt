package com.example.adoptapetmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.adoptapetmobile.ui.components.ImagePicker
import com.example.adoptapetmobile.ui.components.TopBar
import com.example.adoptapetmobile.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProducto(
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val creationSuccess by viewModel.creationSuccess.collectAsState()
    val categorias by viewModel.categorias.collectAsState()

    //Estado de la imagen seleccionada
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

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
            //Selector de imagen
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Imagen del producto",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ImagePicker(
                        selectedImageUri = selectedImageUri,
                        onImageSelected = { uri ->
                            viewModel.updateSelectedImage(uri)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Nombre del producto
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

            //Descripción
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

            //Precio
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

            //Stock
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

            //URL de imagen
            OutlinedTextField(
                value = formState.img,
                onValueChange = { viewModel.onImgChange(it) },
                label = { Text("URL de la imagen (opcional)") },
                isError = formState.imgError != null,
                supportingText = {
                    if (formState.imgError != null) {
                        Text(formState.imgError!!, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("Puedes usar una URL o seleccionar una imagen arriba")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedImageUri == null // Deshabilitado si hay imagen seleccionada
            )

            Spacer(modifier = Modifier.height(12.dp))

            //Selector de categoría DINÁMICO
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

            //Mostrar error general si existe
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

            //Botón guardar
            Button(
                onClick = {
                    viewModel.createProduct(context) // Ahora pasa el contexto
                },
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

    //Diálogo de éxito
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