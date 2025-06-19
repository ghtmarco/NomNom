package com.example.nomnom.`interface`

import com.example.nomnom.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FatSecretApiService {
    @GET("rest/server.api")
    suspend fun searchFoods(
        @Query("method") method: String = "foods.search",
        @Query("format") format: String = "json",
        @Query("search_expression") query: String,
        @Query("page_number") pageNumber: Int = 0,
        @Query("max_results") maxResults: Int = 20,
        @Query("region") region: String = "US"
    ): Response<FoodSearchResponse>

    @GET("rest/server.api")
    suspend fun getFoodDetail(
        @Query("method") method: String = "food.get.v4",
        @Query("format") format: String = "json",
        @Query("food_id") foodId: String,
        @Query("region") region: String = "US"
    ): Response<FoodDetailResponse>

    @GET("rest/server.api")
    suspend fun searchRecipes(
        @Query("method") method: String = "recipes.search",
        @Query("format") format: String = "json",
        @Query("search_expression") query: String,
        @Query("page_number") pageNumber: Int = 0,
        @Query("max_results") maxResults: Int = 10,
        @Query("region") region: String = "US"
    ): Response<RecipeSearchResponse>

    @GET("rest/server.api")
    suspend fun getRecipe(
        @Query("method") method: String = "recipe.get.v2",
        @Query("format") format: String = "json",
        @Query("recipe_id") recipeId: String,
        @Query("region") region: String = "US"
    ): Response<RecipeResponse>
}