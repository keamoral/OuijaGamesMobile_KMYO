package com.example.adoptapetmobile.data.model

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val img: String,
    val resenias: List<Any> = emptyList(),
    val categoria: Categoria
)

data class Categoria(
    val id: Long,
    val nombre: String,
    val descripcion: String
)