package com.proyecto.recipeapp.data

import com.proyecto.recipeapp.data.local.dao.CategoryDao
import com.proyecto.recipeapp.data.local.dao.MealDao
import com.proyecto.recipeapp.data.local.entities.CategoryEntity
import com.proyecto.recipeapp.data.local.entities.MealEntity
import com.proyecto.recipeapp.data.models.MealResponse
import com.proyecto.recipeapp.data.network.MealApiService
import com.proyecto.recipeapp.ui.addRecipe.MealForm
import kotlinx.coroutines.flow.Flow

class MealRepository(
    private val mealApiService: MealApiService,//Aqui llega la implementacion MealApiService hecha por Retrofit
    private val categoryDao: CategoryDao,//Aqui llega la implementacion CategoryDao hecha por Room
    private val mealDao: MealDao//Aqui llega la implementacion MealDao hecha por Room
) {
    /**
     *Local
     */
    fun getMealsStream(): Flow<List<MealEntity>> = mealDao.getAllMeals()
    fun getMealsByName(name: String): Flow<List<MealEntity>> = mealDao.getMealsByName(name)
    fun getMealById(id: Int): Flow<MealEntity?> = mealDao.getMealById(id)
    suspend fun updateMeal(meal: MealEntity) = mealDao.updateMeal(meal)
    suspend fun deleteMeal(meal: MealEntity) = mealDao.deleteMeal(meal)

    //Devuelve un flujo de categorías desde la base de datos local.
    fun getCategoriesStream(): Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    //Agrega la receta a la tabla "meals" en la base de datos local.
    suspend fun addRecipe(meal: MealForm) {
        val mealEntity = MealEntity(
            idMeal = null,
            strMeal = meal.mealName,
            strCategory = meal.mealCategory,
            strInstructions = meal.mealInstructions,
            strMealThumb = meal.mealImage,
            isCustom = true
        )
        mealDao.insertMeal(mealEntity)
    }

    /**
     * Refresca las categorías desde la API si la base de datos local está vacía.
     */
    suspend fun refreshCategories() {
        try {
            val response = mealApiService.getCategories()
            val categories = response.categories ?: emptyList()

            if (categories.isNotEmpty()) {
                val entities = categories.map {
                    CategoryEntity(
                        idCategory = it.idCategory,
                        strCategory = it.strCategory,
                        strCategoryThumb = it.strCategoryThumb
                    )
                }
                categoryDao.insertCategories(entities)
            } else {
                throw Exception("isBlank")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun refreshMeals() {
        try {
            val response = mealApiService.getMealsByName("")
            val meals = response.meals?: emptyList()
            if (meals.isNotEmpty()) {
                val entities = meals.map {
                    MealEntity(
                        idMeal = it.idMeal,
                        strMeal = it.strMeal,
                        strCategory = it.strCategory,
                        strInstructions = it.strInstructions,
                        strMealThumb = it.strMealThumb
                    )
                }
                mealDao.insertMeals(entities)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
