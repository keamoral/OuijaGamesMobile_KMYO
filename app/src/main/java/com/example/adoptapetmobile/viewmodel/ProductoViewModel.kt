package com.example.adoptapetmobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adoptapetmobile.data.model.Categoria
import com.example.adoptapetmobile.data.model.Product
import com.example.adoptapetmobile.data.model.ProductRequest
import com.example.adoptapetmobile.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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

    // Métodos para el formulario
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

        if (state.img.isBlank()) {
            _formState.value = state.copy(imgError = "La URL de la imagen es obligatoria")
            isValid = false
        }

        if (state.categoriaId.isBlank()) {
            isValid = false
        }

        return isValid
    }

    fun createProduct() {
        if (!validateForm()) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val state = _formState.value
                val productRequest = ProductRequest(
                    name = state.name,
                    description = state.description,
                    price = state.price.toInt(),
                    stock = state.stock.toInt(),
                    img = state.img,
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
    }

    fun resetCreationSuccess() {
        _creationSuccess.value = false
    }
}