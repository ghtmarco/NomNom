package com.example.nomnom.data

import com.example.nomnom.model.FoodSearchResponse
import com.example.nomnom.model.FoodDetailResponse
import com.example.nomnom.model.RecipeSearchResponse
import com.example.nomnom.`interface`.FatSecretApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class FoodRepositoryTest {

    private val apiService = mockk<FatSecretApiService>()
    private val repository = FoodRepository(apiService)

    @Test
    fun `searchFoods should return success result when API is successful`() = runBlocking {
        // Arrange
        val query = "apple"
        val mockResponse = mockk<FoodSearchResponse>()
        coEvery { apiService.searchFoods(query = query) } returns Response.success(mockResponse)

        // Act
        val result = repository.searchFoods(query)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockResponse, result.getOrNull())
    }

    @Test
    fun `searchRecipes should return success result when API is successful`() = runBlocking {
        // Arrange
        val query = "pasta"
        val mockResponse = mockk<RecipeSearchResponse>()
        coEvery { apiService.searchRecipes(query = query) } returns Response.success(mockResponse)

        // Act
        val result = repository.searchRecipes(query)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockResponse, result.getOrNull())
    }

    @Test
    fun `getFoodDetail should return success result when API is successful`() = runBlocking {
        // Arrange
        val foodId = "123"
        val mockResponse = mockk<FoodDetailResponse>()
        coEvery { apiService.getFoodDetail(foodId = foodId) } returns Response.success(mockResponse)

        // Act
        val result = repository.getFoodDetail(foodId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockResponse, result.getOrNull())
    }

    @Test
    fun `searchFoods should return failure result when API fails`() = runBlocking {
        // Arrange
        val query = "apple"
        coEvery { apiService.searchFoods(query = query) } returns Response.error(404, mockk(relaxed = true))

        // Act
        val result = repository.searchFoods(query)

        // Assert
        assertTrue(result.isFailure)
    }
}
