package com.example.nomnom.data

import com.example.nomnom.model.FoodSearchResponse
import com.example.nomnom.model.FoodDetailResponse
import com.example.nomnom.model.RecipeResponse
import com.example.nomnom.model.RecipeSearchResponse
import com.example.nomnom.`interface`.FatSecretApiService

class FoodRepository(private val apiService: FatSecretApiService) {

    suspend fun searchFoods(query: String): Result<FoodSearchResponse> {
        return try {
            val response = apiService.searchFoods(query = query)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFoodDetail(foodId: String): Result<FoodDetailResponse> {
        return try {
            val response = apiService.getFoodDetail(foodId = foodId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipe(recipeId: String): Result<RecipeResponse> {
        return try {
            val response = apiService.getRecipe(recipeId = recipeId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchRecipes(query: String): Result<RecipeSearchResponse> {
        return try {
            val response = apiService.searchRecipes(query = query)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
