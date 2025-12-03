package com.proyecto.recipeapp.data.network

import com.proyecto.recipeapp.data.models.CategoryResponse
import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.data.models.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MealApiService {
    /*@GET("filter.php")
    suspend fun getMealsByMainIngredient(
        @Query("i") mainIngredient: String
    ): MealResponse*/

    @GET("lookup.php")
    suspend fun getMealById(
        @Query("i") id: Int
    ): MealResponse

    @GET("search.php")
    suspend fun getMealsByName(
        @Query("s") name: String
    ): MealResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String
    ): MealResponse

}
