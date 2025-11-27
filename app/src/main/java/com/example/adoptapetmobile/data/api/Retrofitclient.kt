package com.example.adoptapetmobile.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // OuijaGames API con logging
    private const val OUIJA_BASE_URL = "https://ouijagames-back.onrender.com/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val ouijaClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val ouijaRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OUIJA_BASE_URL)
            .client(ouijaClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val ouijaApiService: OuijaGamesApiService by lazy {
        ouijaRetrofit.create(OuijaGamesApiService::class.java)
    }
}
