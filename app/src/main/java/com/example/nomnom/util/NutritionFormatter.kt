package com.example.nomnom.util

object NutritionFormatter {

    fun formatCalories(value: String?): String {
        return if (value.isNullOrEmpty()) "0 Calories" else "$value Calories"
    }

    fun formatGram(value: String?): String {
        return if (value.isNullOrEmpty()) "0g" else "${value}g"
    }

    fun formatMilligram(value: String?): String {
        return if (value.isNullOrEmpty()) "0mg" else "${value}mg"
    }
}
