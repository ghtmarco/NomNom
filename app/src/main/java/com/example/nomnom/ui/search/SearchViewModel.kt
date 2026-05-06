package com.example.nomnom.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nomnom.data.FoodRepository
import com.example.nomnom.model.FoodData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val foods: List<FoodData>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class SearchViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun searchFoods(query: String) {
        if (query.trim().isEmpty()) return

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            repository.searchFoods(query.trim())
                .onSuccess { response ->
                    val foodItems = response.foods?.food ?: emptyList()
                    val foodDataList = foodItems.map { foodItem ->
                        FoodData(
                            id = foodItem.food_id,
                            name = foodItem.food_name,
                            brandName = foodItem.brand_name,
                            imageUrl = null,
                            calories = null,
                            nutrition = null
                        )
                    }
                    _uiState.value = SearchUiState.Success(foodDataList)
                }
                .onFailure {
                    _uiState.value = SearchUiState.Error(it.message ?: "Search failed")
                }
        }
    }
}
