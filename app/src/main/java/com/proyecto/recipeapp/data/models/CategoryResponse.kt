package com.proyecto.recipeapp.data.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
data class CategoryResponse(
    val categories: List<Category>?
)

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String
)
