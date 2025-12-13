package com.example.adoptapetmobile.data.repository

import com.example.adoptapetmobile.data.api.RetrofitClient
import com.example.adoptapetmobile.data.model.Categoria
import com.example.adoptapetmobile.data.model.Product
import com.example.adoptapetmobile.data.model.ProductRequest

class ProductRepository {

    private val api = RetrofitClient.ouijaApiService

    suspend fun getProducts(): List<Product> {
        val response = api.getProducts()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener productos: ${response.code()}")
        }
    }

    suspend fun getProductById(id: Int): Product? {
        val response = api.getProductById(id)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun createProduct(productRequest: ProductRequest): Product? {
        try {
            val response = api.createProduct(productRequest)
            return if (response.isSuccessful) {
                response.body()
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin mensaje de error"
                throw Exception("Error ${response.code()}: $errorBody")
            }
        } catch (e: Exception) {
            throw Exception("Error de red: ${e.message}")
        }
    }

    suspend fun deleteProduct(id: Long): Boolean {
        try {
            val response = api.deleteProduct(id)
            return response.isSuccessful
        } catch (e: Exception) {
            throw Exception("Error al eliminar producto: ${e.message}")
        }
    }

    suspend fun getCategorias(): List<Categoria> {
        val response = api.getCategorias()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener categorías: ${response.code()}")
        }
    }
}