package com.example.nomnom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.nomnom.R
import com.example.nomnom.model.*
import com.example.nomnom.`interface`.ApiClient
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nomnom.data.FoodRepository
import com.example.nomnom.ui.details.DetailsUiState
import com.example.nomnom.ui.details.DetailsViewModel
import com.example.nomnom.ui.details.DetailsViewModelFactory
import com.example.nomnom.util.NutritionFormatter
import com.example.nomnom.ui.components.CustomButton
import com.example.nomnom.ui.theme.Blue
import com.example.nomnom.ui.theme.*
import kotlinx.coroutines.launch

class DetailsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val foodId = intent.getStringExtra("food_id")
        val foodName = intent.getStringExtra("food_name") ?: ""
        val brandName = intent.getStringExtra("brand_name")
        val imageUrl = intent.getStringExtra("image_url")
        val isRecipe = intent.getBooleanExtra("is_recipe", false)

        val recipeId = intent.getStringExtra("recipe_id")
        val prepTime = intent.getStringExtra("prep_time")
        val description = intent.getStringExtra("description")
        val calories = intent.getStringExtra("calories")
        val protein = intent.getStringExtra("protein")
        val carbs = intent.getStringExtra("carbs")
        val fat = intent.getStringExtra("fat")

        setContent {
            NomNomTheme {
                val repository = remember { FoodRepository(ApiClient.api) }
                val viewModel: DetailsViewModel = viewModel(
                    factory = DetailsViewModelFactory(repository)
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isRecipe) {
                        RecipeDetailsScreen(
                            viewModel = viewModel,
                            recipeId = recipeId,
                            foodName = foodName,
                            imageUrl = imageUrl,
                            prepTime = prepTime,
                            description = description,
                            calories = calories,
                            protein = protein,
                            carbs = carbs,
                            fat = fat,
                            onBackPressed = { finish() }
                        )
                    } else {
                        FoodDetailsScreen(
                            viewModel = viewModel,
                            foodId = foodId ?: "",
                            foodName = foodName,
                            brandName = brandName,
                            onBackPressed = { finish() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodDetailsScreen(
    viewModel: DetailsViewModel,
    foodId: String,
    foodName: String,
    brandName: String?,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(foodId) {
        viewModel.getFoodDetail(foodId)
    }

    val foodDetail = (uiState as? DetailsUiState.Success)?.food?.food
    val isLoading = uiState is DetailsUiState.Loading
    val error = (uiState as? DetailsUiState.Error)?.message

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        }
        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error loading food details",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = error ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomButton(
                        text = "Go Back",
                        onClick = onBackPressed
                    )
                }
            }
        }
        else -> {
            foodDetail?.let { food ->
                val serving = food.servings?.serving?.firstOrNull()
                FoodDetailContent(
                    foodName = food.food_name,
                    brandName = food.brand_name,
                    imageUrl = food.food_images?.food_image,
                    serving = serving,
                    onBackPressed = onBackPressed
                )
            }
        }
    }
}

@Composable
fun FoodDetailContent(
    foodName: String,
    brandName: String?,
    imageUrl: String?,
    serving: Serving?,
    onBackPressed: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = imageUrl ?: R.drawable.food_image,
                    contentDescription = foodName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.food_image)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = CardBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = foodName,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    if (!brandName.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = brandName,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                    }

                    serving?.let { servingData ->
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Serving Size",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = servingData.serving_description ?: "1 serving",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Nutrition Facts",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        NutritionGrid(serving = servingData)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    CustomButton(
                        text = "Add to Meal Plan",
                        onClick = { /* Add to meal plan */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDetailsScreen(
    viewModel: DetailsViewModel,
    recipeId: String?,
    foodName: String,
    imageUrl: String?,
    prepTime: String?,
    description: String?,
    calories: String?,
    protein: String?,
    carbs: String?,
    fat: String?,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(recipeId) {
        if (!recipeId.isNullOrEmpty()) {
            viewModel.getRecipe(recipeId)
        }
    }

    val fullRecipe = (uiState as? DetailsUiState.Success)?.recipe?.recipe
    val isLoading = uiState is DetailsUiState.Loading
    val error = (uiState as? DetailsUiState.Error)?.message

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Error: $error", color = Color.Red)
                    Button(onClick = { if (!recipeId.isNullOrEmpty()) viewModel.getRecipe(recipeId) }) {
                        Text("Retry")
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = imageUrl?.takeIf { it.isNotBlank() } ?: R.drawable.food_image,
                    contentDescription = foodName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(id = R.drawable.food_image),
                    placeholder = painterResource(id = R.drawable.food_image)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                color = CardBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = foodName,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    if (!description.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                    }

                    if (!prepTime.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PrimaryColor.copy(alpha = 0.1f),
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.food_image),
                                    contentDescription = null,
                                    tint = PrimaryColor,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Prep time: $prepTime",
                                style = MaterialTheme.typography.titleMedium,
                                color = PrimaryColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (!calories.isNullOrEmpty() || !protein.isNullOrEmpty() ||
                        !carbs.isNullOrEmpty() || !fat.isNullOrEmpty()) {

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Nutrition Facts",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        RecipeNutritionGrid(
                            calories = calories,
                            carbs = carbs,
                            protein = protein,
                            fat = fat
                        )
                    }

                    fullRecipe?.let { recipe ->
                        Spacer(modifier = Modifier.height(24.dp))

                        recipe.ingredients?.ingredient?.takeIf { it.isNotEmpty() }?.let { ingredients ->
                            Text(
                                text = "Ingredients",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            ingredients.forEach { ingredient ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Blue)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = ingredient.ingredient_description ?: ingredient.food_name ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        recipe.directions?.direction?.takeIf { it.isNotEmpty() }?.let { directions ->
                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Directions",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            directions.sortedBy { it.direction_number?.toIntOrNull() ?: 0 }.forEach { direction ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = PrimaryColor,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = direction.direction_number ?: "1",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = direction.direction_description ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextSecondary,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    CustomButton(
                        text = "Save Recipe",
                        onClick = { /* Save recipe */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeNutritionGrid(
    calories: String?,
    carbs: String?,
    protein: String?,
    fat: String?
) {
    val nutritionItems = listOf(
        "Calories" to NutritionFormatter.formatCalories(calories),
        "Carbs" to NutritionFormatter.formatGram(carbs),
        "Protein" to NutritionFormatter.formatGram(protein),
        "Fat" to NutritionFormatter.formatGram(fat)
    )

    Column {
        nutritionItems.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { (label, value) ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = SecondaryBackground,
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = value,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryColor
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun NutritionGrid(serving: Serving) {
    val nutritionItems = listOf(
        "Calories" to serving.calories,
        "Carbs" to serving.carbohydrate?.let { "${it}g" },
        "Protein" to serving.protein?.let { "${it}g" },
        "Fat" to serving.fat?.let { "${it}g" },
        "Fiber" to serving.fiber?.let { "${it}g" },
        "Sugar" to serving.sugar?.let { "${it}g" },
        "Sodium" to serving.sodium?.let { "${it}mg" },
        "Cholesterol" to serving.cholesterol?.let { "${it}mg" }
    ).filter { it.second != null }

    Column {
        nutritionItems.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { (label, value) ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = SecondaryBackground,
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = value ?: "0",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryColor
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}