package com.example.nomnom.fragment

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nomnom.DetailsActivity
import com.example.nomnom.model.*
import com.example.nomnom.`interface`.ApiClient
import com.example.nomnom.ui.components.*
import com.example.nomnom.ui.theme.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

import com.example.nomnom.ui.product.ProductViewModel
import com.example.nomnom.ui.product.ProductUiState

@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.loadRecipes()
    }

    val recipes = (uiState as? ProductUiState.Success)?.recipes ?: emptyList()
    val isLoading = uiState is ProductUiState.Loading
    val error = (uiState as? ProductUiState.Error)?.message

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardBackground.copy(alpha = 0.95f),
            tonalElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Discover",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Curated recipes with nutritional info",
                    fontSize = 16.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                SearchBar(
                    hint = "Search for foods and recipes...",
                    onClick = onSearchClick
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PrimaryColor,
                            modifier = Modifier.size(60.dp),
                            strokeWidth = 5.dp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Finding curated recipes...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                    }
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.large,
                        color = CardBackground,
                        tonalElevation = 4.dp,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = PrimaryColor,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Connection Issue",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error ?: "An error occurred",
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            CustomButton(
                                text = "Try Again",
                                onClick = { viewModel.loadRecipes() },
                                icon = Icons.Default.Refresh
                            )
                        }
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Section(
                        title = "Curated Recipes",
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        if (recipes.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No recipes found",
                                        color = TextSecondary,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Check your internet connection and try again",
                                        color = TextSecondary.copy(alpha = 0.7f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                modifier = Modifier.height(600.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(recipes) { recipe ->
                                    RecipeCard(
                                        name = recipe.name,
                                        imageUrl = recipe.imageUrl,
                                        prepTime = recipe.totalTime ?: "30 min",
                                        onClick = {
                                            val intent = Intent(context, DetailsActivity::class.java).apply {
                                                putExtra("recipe_id", recipe.id)
                                                putExtra("food_name", recipe.name)
                                                putExtra("prep_time", recipe.totalTime)
                                                putExtra("image_url", recipe.imageUrl)
                                                putExtra("description", recipe.description)
                                                putExtra("calories", recipe.nutrition?.calories)
                                                putExtra("protein", recipe.nutrition?.protein)
                                                putExtra("carbs", recipe.nutrition?.carbohydrate)
                                                putExtra("fat", recipe.nutrition?.fat)
                                                putExtra("is_recipe", true)
                                            }
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}