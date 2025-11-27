package com.example.adoptapetmobile.data.model

data class ProductRequest(
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val img: String,
    val categoriaId: Long
)

