package com.example.nomnom.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomnom.data.FoodRepository
import com.example.nomnom.model.RecipeData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val recipes: List<RecipeData>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}

class ProductViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val targetRecipes = listOf(
        "Mustard Crusted Steak",
        "Balsamic Salmon",
        "Baked Salmon",
        "Basil Chicken Primavera",
        "Spinach Feta Pasta Shrimp",
        "Hibiscus Tea",
        "Oatmeal Chocolate Chip Cookies",
        "Protein Shake",
        "Bang Bang Shrimp Salad",
        "Buffalo Chicken Flatbread"
    )

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading
            try {
                val searchTasks = targetRecipes.map { searchTerm ->
                    async {
                        repository.searchRecipes(searchTerm).getOrNull()?.recipes?.recipe?.firstOrNull()
                    }
                }

                val results = searchTasks.awaitAll().filterNotNull()

                val recipeDataList = results.map { recipeItem ->
                    RecipeData(
                        id = recipeItem.recipe_id,
                        name = recipeItem.recipe_name,
                        description = recipeItem.recipe_description,
                        prepTime = null,
                        cookTime = null,
                        totalTime = "~30 min",
                        imageUrl = recipeItem.recipe_image?.takeIf { it.isNotBlank() },
                        nutrition = recipeItem.recipe_nutrition,
                        servings = null,
                        rating = null,
                        ingredients = null,
                        directions = null
                    )
                }

                if (recipeDataList.isEmpty()) {
                    _uiState.value = ProductUiState.Error("No recipes found. Please check your connection.")
                } else {
                    _uiState.value = ProductUiState.Success(recipeDataList)
                }
            } catch (e: Exception) {
                _uiState.value = ProductUiState.Error(e.message ?: "Failed to load recipes")
            }
        }
    }
}
