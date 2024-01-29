package com.example.recipesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipesapp.presentation.details.RecipeDetailScreen
import com.example.recipesapp.presentation.list.RecipesListScreen
import com.example.recipesapp.presentation.list.RecipesViewModel
import com.example.recipesapp.ui.theme.RecipesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipesApp()
                }
            }
        }
    }
}

@Composable
fun RecipesApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "recipes"){
        composable(route = "recipes") {
            val viewModel: RecipesViewModel = viewModel()
            RecipesListScreen (
                state = viewModel.state.value,
                onItemClick = {id ->
                    navController.navigate("recipes/$id")},
                onFavoriteClick = { id, oldValue ->
                    viewModel.toggleFavorite(id, oldValue)
                })
        }
        composable(
            route = "recipes/{recipe_id}",
            arguments = listOf(navArgument("recipe_id") {type = NavType.IntType})
        ){
            RecipeDetailScreen()
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecipesAppTheme {
        Greeting("Android")
    }
}