package com.example.adoptapetmobile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptapetmobile.data.model.Categoria
import com.example.adoptapetmobile.data.model.Product
import com.example.adoptapetmobile.data.model.ProductRequest
import com.example.adoptapetmobile.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


data class ProductFormState(
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val stock: String = "",
    val img: String = "",
    val categoriaId: String = "",
    val nameError: String? = null,
    val descriptionError: String? = null,
    val priceError: String? = null,
    val stockError: String? = null,
    val imgError: String? = null
)

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _formState = MutableStateFlow(ProductFormState())
    val formState: StateFlow<ProductFormState> = _formState

    private val _creationSuccess = MutableStateFlow(false)
    val creationSuccess: StateFlow<Boolean> = _creationSuccess

    //Estado para la imagen seleccionada
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    init {
        loadProducts()
        loadCategorias()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCategorias() {
        viewModelScope.launch {
            try {
                _categorias.value = repository.getCategorias()
                // Si hay categorías, establece la primera por defecto
                if (_categorias.value.isNotEmpty()) {
                    _formState.value = _formState.value.copy(
                        categoriaId = _categorias.value.first().id.toString()
                    )
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar categorías: ${e.message}"
            }
        }
    }

    fun loadProductById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _selectedProduct.value = repository.getProductById(id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Función para actualizar la imagen seleccionada
    fun updateSelectedImage(uri: Uri?) {
        _selectedImageUri.value = uri
        // Si se selecciona una imagen, limpiar el error de URL
        if (uri != null) {
            _formState.value = _formState.value.copy(imgError = null)
        }
    }

    //Métodos para el formulario
    fun onNameChange(name: String) {
        _formState.value = _formState.value.copy(
            name = name,
            nameError = null
        )
    }

    fun onDescriptionChange(description: String) {
        _formState.value = _formState.value.copy(
            description = description,
            descriptionError = null
        )
    }

    fun onPriceChange(price: String) {
        _formState.value = _formState.value.copy(
            price = price,
            priceError = null
        )
    }

    fun onStockChange(stock: String) {
        _formState.value = _formState.value.copy(
            stock = stock,
            stockError = null
        )
    }

    fun onImgChange(img: String) {
        _formState.value = _formState.value.copy(
            img = img,
            imgError = null
        )
    }

    fun onCategoriaIdChange(categoriaId: String) {
        _formState.value = _formState.value.copy(categoriaId = categoriaId)
    }

    fun validateForm(): Boolean {
        val state = _formState.value
        var isValid = true

        if (state.name.isBlank()) {
            _formState.value = state.copy(nameError = "El nombre es obligatorio")
            isValid = false
        }

        if (state.description.isBlank()) {
            _formState.value = state.copy(descriptionError = "La descripción es obligatoria")
            isValid = false
        }

        val priceInt = state.price.toIntOrNull()
        if (priceInt == null || priceInt <= 0) {
            _formState.value = state.copy(priceError = "Ingrese un precio válido mayor a 0")
            isValid = false
        }

        val stockInt = state.stock.toIntOrNull()
        if (stockInt == null || stockInt < 0) {
            _formState.value = state.copy(stockError = "Ingrese un stock válido (0 o mayor)")
            isValid = false
        }

        //Validar imagen (URL o imagen seleccionada)
        if (state.img.isBlank() && _selectedImageUri.value == null) {
            _formState.value = state.copy(imgError = "Debes agregar una imagen o URL")
            isValid = false
        }

        if (state.categoriaId.isBlank()) {
            isValid = false
        }

        return isValid
    }

    fun createProduct(context: Context? = null) {
        if (!validateForm()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val state = _formState.value

                // Si hay una imagen URI seleccionada 
                val imageUrl = if (_selectedImageUri.value != null && context != null) {
                    uploadImageToServer(context, _selectedImageUri.value!!)
                } else {
                    state.img
                }

                val productRequest = ProductRequest(
                    name = state.name,
                    description = state.description,
                    price = state.price.toInt(),
                    stock = state.stock.toInt(),
                    img = imageUrl,
                    categoriaId = state.categoriaId.toLong()
                )

                repository.createProduct(productRequest)
                _creationSuccess.value = true
                resetForm()
                loadProducts()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear el producto"
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Función para subir imagen al servidor
    private suspend fun uploadImageToServer(context: Context, uri: Uri): String {
        // si hay una URL en el campo img
        if (_formState.value.img.isNotBlank()) {
            return _formState.value.img
        }

        // Si no hay URL, convertir a File y retornar path local
        val file = uriToFile(context, uri)
        return file?.absolutePath ?: ""
    }

    //Función para convertir URI a File
    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val success = repository.deleteProduct(productId)
                if (success) {
                    loadProducts()
                } else {
                    _error.value = "No se pudo eliminar el producto"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al eliminar el producto"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetForm() {
        val defaultCategoriaId = if (_categorias.value.isNotEmpty()) {
            _categorias.value.first().id.toString()
        } else {
            ""
        }
        _formState.value = ProductFormState(categoriaId = defaultCategoriaId)
        _selectedImageUri.value = null // Limpiar imagen seleccionada
    }

    fun resetCreationSuccess() {
        _creationSuccess.value = false
    }
}
