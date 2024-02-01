package com

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.recipesapp.DummyContent
import com.example.recipesapp.presentation.Description
import com.example.recipesapp.presentation.RecipesApp
import com.example.recipesapp.presentation.list.RecipeScreenState
import com.example.recipesapp.presentation.list.RecipesListScreen
import com.example.recipesapp.ui.theme.RecipesAppTheme
import org.junit.Rule
import org.junit.Test

class RecipesScreenTest {
    @get:Rule
    val testRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun initialState_isRendered() {
        testRule.setContent {
            RecipesAppTheme {
                RecipesListScreen(
                    state = RecipeScreenState(
                        recipes = emptyList(),
                        isLoading = true
                    ),
                    onFavoriteClick = {
                        _: Int, _: Boolean ->
                    },
                    onItemClick = {}
                )
            }
        }
        testRule.onNodeWithContentDescription(Description.RECIPES_LOADING).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val recipes = DummyContent.getDomainRecipes()
        testRule.setContent {
            RecipesAppTheme {
                RecipesListScreen(
                    state = RecipeScreenState(
                        recipes = recipes,
                        isLoading = false
                    ),
                    onFavoriteClick = {
                            _: Int, _: Boolean ->
                    },
                    onItemClick = {}
                )
            }
        }
        testRule.onNodeWithText(recipes[0].title).assertIsDisplayed()
        testRule.onNodeWithText(recipes[0].description).assertIsDisplayed()
        testRule.onNodeWithContentDescription(Description.RECIPES_LOADING).assertDoesNotExist()
    }

}