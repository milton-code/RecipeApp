package com.proyecto.recipeapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.proyecto.recipeapp.data.network.MealApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainer {
    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: MealApiService by lazy { retrofit.create(MealApiService::class.java) }

    val mealRepository: MealRepository by lazy {
        MealRepository(retrofitService)
    }
}