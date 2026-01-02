package com.proyecto.recipeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.proyecto.recipeapp.data.local.entities.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("DELETE FROM meals WHERE isCustom = 0")
    suspend fun deleteAllApiMeals()

    @Query("SELECT * FROM meals WHERE isCustom = 1")
    fun getLocalMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals ORDER BY strMeal ASC")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE id = :id")
    fun getMealById(id: Int): Flow<MealEntity>
}
