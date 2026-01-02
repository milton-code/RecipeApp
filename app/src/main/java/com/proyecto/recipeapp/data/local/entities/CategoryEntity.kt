package com.proyecto.recipeapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String
)
