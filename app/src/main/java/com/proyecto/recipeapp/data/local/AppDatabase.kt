package com.proyecto.recipeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proyecto.recipeapp.data.local.dao.CategoryDao
import com.proyecto.recipeapp.data.local.dao.MealDao
import com.proyecto.recipeapp.data.local.entities.CategoryEntity
import com.proyecto.recipeapp.data.local.entities.MealEntity

@Database(entities = [MealEntity::class, CategoryEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao
    abstract fun categoryDao(): CategoryDao


    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
