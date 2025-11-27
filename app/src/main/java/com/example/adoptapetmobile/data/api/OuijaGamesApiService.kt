package com.example.adoptapetmobile.data.api

import com.example.adoptapetmobile.data.model.Categoria
import com.example.adoptapetmobile.data.model.Product
import com.example.adoptapetmobile.data.model.ProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OuijaGamesApiService {

    @GET("products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @POST("products")
    suspend fun createProduct(@Body product: ProductRequest): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<Unit>

    @GET("categories")
    suspend fun getCategorias(): Response<List<Categoria>>
}