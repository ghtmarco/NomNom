package com.example.nomnom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nomnom.data.FoodRepository
import com.example.nomnom.`interface`.ApiClient
import com.example.nomnom.fragment.ProductScreen
import com.example.nomnom.fragment.SearchScreen
import com.example.nomnom.ui.product.ProductViewModel
import com.example.nomnom.ui.product.ProductViewModelFactory
import com.example.nomnom.ui.search.SearchViewModel
import com.example.nomnom.ui.search.SearchViewModelFactory
import com.example.nomnom.ui.theme.NomNomTheme
import com.example.nomnom.ui.theme.PrimaryColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NomNomTheme {
                MainScreen()
            }
        }
    }
}

enum class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home("Home", Icons.Filled.Home, Icons.Outlined.Home)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedNavItem by remember { mutableStateOf(BottomNavItem.Home) }
    var showSearch by remember { mutableStateOf(false) }

    val repository = remember { FoodRepository(ApiClient.api) }
    val productViewModel: ProductViewModel = viewModel(
        factory = ProductViewModelFactory(repository)
    )
    val searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(repository)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NomNom",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        if (showSearch) {
            SearchScreen(
                viewModel = searchViewModel,
                modifier = Modifier.padding(innerPadding),
                onBackToHome = { showSearch = false }
            )
        } else {
            ProductScreen(
                viewModel = productViewModel,
                modifier = Modifier.padding(innerPadding),
                onSearchClick = { showSearch = true }
            )
        }
    }
}