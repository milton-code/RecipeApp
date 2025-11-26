package com.proyecto.recipeapp.data

import com.proyecto.recipeapp.data.models.Meal
import com.proyecto.recipeapp.data.models.MealResponse
import com.proyecto.recipeapp.data.network.MealApiService

class MealRepository (private val mealApiService: MealApiService){
    //el argumento que llega es la implementacion de MealApiService por parte de retrofit
    suspend fun getMealsByMainIngredient(mainIngredient: String): MealResponse = mealApiService.getMealsByMainIngredient(mainIngredient)
    suspend fun getMealById(id: Int): MealResponse = mealApiService.getMealById(id)

    suspend fun getMealsByName(name: String): MealResponse = mealApiService.getMealsByName(name)
}


