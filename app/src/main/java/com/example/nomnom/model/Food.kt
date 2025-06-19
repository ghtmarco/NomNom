package com.example.nomnom.model

data class Food(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val brandName: String? = null,
    val description: String? = null
)

data class FoodSearchResponse(
    val foods: Foods?
)

data class Foods(
    val food: List<FoodItem>?,
    val max_results: String?,
    val page_number: String?,
    val total_results: String?
)

data class FoodItem(
    val food_id: String,
    val food_name: String,
    val brand_name: String?,
    val food_type: String?,
    val food_url: String?
)

data class FoodDetailResponse(
    val food: FoodDetail?
)

data class FoodDetail(
    val food_id: String,
    val food_name: String,
    val brand_name: String?,
    val food_type: String?,
    val food_images: FoodImages?,
    val servings: Servings?
)

data class FoodImages(
    val food_image: String?
)

data class Servings(
    val serving: List<Serving>?
)

data class Serving(
    val serving_id: String?,
    val serving_description: String?,
    val serving_url: String?,
    val metric_serving_amount: String?,
    val metric_serving_unit: String?,
    val number_of_units: String?,
    val measurement_description: String?,
    val calories: String?,
    val carbohydrate: String?,
    val protein: String?,
    val fat: String?,
    val saturated_fat: String?,
    val polyunsaturated_fat: String?,
    val monounsaturated_fat: String?,
    val trans_fat: String?,
    val cholesterol: String?,
    val sodium: String?,
    val potassium: String?,
    val fiber: String?,
    val sugar: String?,
    val vitamin_a: String?,
    val vitamin_c: String?,
    val calcium: String?,
    val iron: String?
)

data class RecipeSearchResponse(
    val recipes: RecipeSearchResult?
)

data class RecipeSearchResult(
    val recipe: List<RecipeItem>?,
    val max_results: String?,
    val page_number: String?,
    val total_results: String?
)

data class RecipeItem(
    val recipe_id: String,
    val recipe_name: String,
    val recipe_url: String,
    val recipe_description: String?,
    val recipe_image: String?,
    val recipe_nutrition: RecipeNutrition?
)

data class RecipeNutrition(
    val calories: String?,
    val carbohydrate: String?,
    val protein: String?,
    val fat: String?
)

data class RecipeResponse(
    val recipe: Recipe?
)

data class Recipe(
    val recipe_id: String,
    val recipe_name: String,
    val recipe_url: String?,
    val recipe_description: String?,
    val recipe_images: RecipeImages?,
    val cooking_time_min: String?,
    val preparation_time_min: String?,
    val number_of_servings: String?,
    val rating: String?,
    val ingredients: Ingredients?,
    val directions: Directions?,
    val recipe_nutrition: RecipeNutrition?
)

data class RecipeImages(
    val recipe_image: String?
)

data class Ingredients(
    val ingredient: List<Ingredient>?
)

data class Ingredient(
    val food_id: String?,
    val food_name: String?,
    val ingredient_description: String?,
    val ingredient_url: String?,
    val measurement_description: String?,
    val number_of_units: String?,
    val serving_id: String?
)

data class Directions(
    val direction: List<Direction>?
)

data class Direction(
    val direction_number: String?,
    val direction_description: String?
)

data class ServingSizes(
    val serving: Map<String, Any>
)

data class FoodData(
    val id: String,
    val name: String,
    val brandName: String?,
    val imageUrl: String?,
    val calories: String?,
    val nutrition: NutritionInfo?
)

data class NutritionInfo(
    val calories: String?,
    val carbs: String?,
    val protein: String?,
    val fat: String?,
    val fiber: String?,
    val sugar: String?,
    val sodium: String?,
    val servingDescription: String?
)

data class RecipeData(
    val id: String,
    val name: String,
    val description: String?,
    val prepTime: String?,
    val cookTime: String?,
    val totalTime: String?,
    val imageUrl: String?,
    val nutrition: RecipeNutrition?,
    val servings: String?,
    val rating: String?,
    val ingredients: String?,
    val directions: String?
)