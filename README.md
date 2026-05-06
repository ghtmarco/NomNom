<p align="center">
  <img src="screenshots/logo.png" width="128" alt="NomNom Logo">
</p>

# NomNom: Food & Recipes for Android

NomNom helps you find what's in your food. Search for ingredients, browse recipes, and get nutritional data without the fluff. Built with Jetpack Compose and a focus on clean, testable code.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)

---

## What it does

- **Food Search**: Get calories, protein, carbs, and fats for almost anything.
- **Recipe Discovery**: A curated list of recipes with full nutritional breakdowns.
- **Clean UI**: No cluttered menus. Just a simple, Material 3 interface that gets out of your way.

---

## Screenshots

<p align="center">
  <img src="screenshots/SplashScreen.png.png" width="200" alt="Splash">
  <img src="screenshots/HomeScreen.png.png" width="200" alt="Home">
  <img src="screenshots/SearchScreen.png.png" width="200" alt="Search">
  <img src="screenshots/DetailScreen.png.png" width="200" alt="Detail">
</p>

---

## Tech Stack

I built this using a Clean Architecture approach to keep the UI separate from the data logic. It's easier to test and maintain this way.

- **Frontend**: Jetpack Compose with Material 3.
- **Networking**: Retrofit and OkHttp.
- **Architecture**: MVVM with a Repository pattern.
- **Images**: Coil for async image loading.
- **Testing**: JUnit and MockK (unit tests cover the repository and formatting logic).

```mermaid
graph LR
    UI[Compose UI] --> VM[ViewModel]
    VM --> Repo[Repository]
    Repo --> API[FatSecret API]
```

---

## Getting Started

### 1. API Keys
You'll need a developer account at [FatSecret Platform](https://platform.fatsecret.com/). 

### 2. Configuration
Add your keys to `local.properties` in the root directory:
```properties
fatsecret.consumer.key=your_key_here
fatsecret.consumer.secret=your_secret_here
```

### 3. Build
Open in Android Studio (Jellyfish+) and hit run. Make sure you're using JDK 17.

---

## Development

The project follows a TDD workflow. You can run the tests with:
```bash
./gradlew test
```

---

Made by Asphyxia & Antigravity
