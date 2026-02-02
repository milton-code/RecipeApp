# RecipeApp

This Android application uses TheMealDB's REST API to query an initial list of recipes and a list of recipe categories.

The application uses a local SQLite database implemented with Room. It follows an offline-first architecture where the database is the single source of truth and acts as a disk cache, storing the results of API queries.

The app allows users to view retrieved recipes and their details, add them to favorites, and create custom recipes by storing them in the database. Users can also delete custom recipes from the database if desired.

The app includes a search bar to find recipes by name, as well as filters to display favorite recipes, user-created recipes, or recipes by category.

## Key Features

- MVVM architecture implementing offline-first
- Using the Retrofit library to consume a REST API
- Using the Room library to implement an SQLite database
- Using the Coil library for loading images in the UI
- Using Flows and coroutines to manage a reactive UI
- Handling exceptions such as connection errors
- Reactive search bar

## Tech Stack

**Client:** Kotlin, Jetpack Compose, Material Design, Retrofit, Room, SQLite, Coil, Flows, Coroutines, JSON, Gradle

**Server:** REST API

**Architectural Pattern:** Model - View - ViewModel, Offline-first

## Screenshots

![App Screenshot](https://github.com/milton-code/RecipeApp/blob/main/readme%20screenshots/home_screen.png)
![App Screenshot](https://github.com/milton-code/RecipeApp/blob/main/readme%20screenshots/detail_screen.png)
![App Screenshot](https://github.com/milton-code/RecipeApp/blob/main/readme%20screenshots/addrecipe_screen.png)
