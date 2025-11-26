package com.proyecto.recipeapp.data.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
data class MealResponse(
    val meals: List<Meal>?
)
@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Meal(
    val idMeal: Int = 0,
    val strMeal: String = "",
    val strCategory: String = "",
    val strArea: String = "",
    val strInstructions: String = "",
    val strMealThumb: String = "",
    @SerialName("strIngredient1")
    val mainIngredient: String = ""
)



