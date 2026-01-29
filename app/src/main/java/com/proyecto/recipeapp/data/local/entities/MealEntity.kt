package com.proyecto.recipeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idMeal: Int?, // From TheMealDB API, nullable for local-only recipes
    val strMeal: String,
    val strCategory: String,
    val strInstructions: String,
    val strMealThumb: String,
    val isCustom: Boolean = false, // True for user-created recipes
    val isFavorite: Boolean = false
)
