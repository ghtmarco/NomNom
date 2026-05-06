package com.example.nomnom.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomnom.data.FoodRepository
import com.example.nomnom.model.FoodDetailResponse
import com.example.nomnom.model.RecipeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(val recipe: RecipeResponse? = null, val food: FoodDetailResponse? = null) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}

class DetailsViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun getRecipe(recipeId: String) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            repository.getRecipe(recipeId)
                .onSuccess { recipe ->
                    _uiState.value = DetailsUiState.Success(recipe = recipe)
                }
                .onFailure { error ->
                    _uiState.value = DetailsUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun getFoodDetail(foodId: String) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            repository.getFoodDetail(foodId)
                .onSuccess { food ->
                    _uiState.value = DetailsUiState.Success(food = food)
                }
                .onFailure { error ->
                    _uiState.value = DetailsUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
