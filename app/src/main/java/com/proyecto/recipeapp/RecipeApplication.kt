package com.proyecto.recipeapp

import android.app.Application
import com.proyecto.recipeapp.data.AppContainer

class RecipeApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}