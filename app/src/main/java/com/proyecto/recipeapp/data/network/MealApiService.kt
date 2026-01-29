package com.proyecto.recipeapp.data.network

import com.proyecto.recipeapp.data.models.CategoryResponse
import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.data.models.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MealApiService {
    @GET("search.php")
    suspend fun getMealsByName(
        @Query("s") name: String
    ): MealResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse
}
