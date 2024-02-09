package com.example.recipesapp

import com.FakeApiService
import com.FakeRoomDao
import com.example.recipesapp.data.RecipesRepository
import com.example.recipesapp.domain.GetInitialRecipesUseCase
import com.example.recipesapp.domain.GetSortedRecipesUseCase
import com.example.recipesapp.domain.ToggleFavoriteRecipeUseCase
import com.example.recipesapp.presentation.list.RecipeScreenState
import com.example.recipesapp.presentation.list.RecipesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipesViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    private val error: Exception = Exception()
    @Test
    fun initialState_isProduced() = scope.runTest {
        // subject under test
        val viewModel = getViewModel()
        val initialState = viewModel.state.value

        assert(initialState == RecipeScreenState(
            recipes = emptyList(),
            isLoading = true,
            error = null
        ))
    }

    @Test
    fun stateWithContent_isProduced() = scope.runTest {
        val viewModel = getViewModel()
        advanceUntilIdle()
        val currentState = viewModel.state.value
        assert(
            currentState == RecipeScreenState(
                recipes = DummyContent.getDomainRecipes(),
                isLoading = false,
                error = null
            )
        )
    }

    @Test
    fun errorState_isProduced() = scope.runTest {
        val viewModel = getViewModelWithError()
        advanceUntilIdle()
        val currentState = viewModel.state.value
        assert(
            currentState == RecipeScreenState(
                recipes = emptyList(),
                isLoading = false,
                error = error.message
            )
        )
    }

    private fun getViewModel(): RecipesViewModel {

        val recipesRepository = RecipesRepository(FakeApiService(), FakeRoomDao(), dispatcher)
        val getSortedRecipesUseCase = GetSortedRecipesUseCase(recipesRepository)
        val getInitialRecipesUseCase = GetInitialRecipesUseCase(recipesRepository, getSortedRecipesUseCase)
        val toggleRecipesUseCase = ToggleFavoriteRecipeUseCase(recipesRepository, getSortedRecipesUseCase)

        return RecipesViewModel(getInitialRecipesUseCase, toggleRecipesUseCase, dispatcher)
    }

    private fun getViewModelWithError(): RecipesViewModel {


        val recipesRepository = RecipesRepository(FakeApiService(error), FakeRoomDao(), dispatcher)
        val getSortedRecipesUseCase = GetSortedRecipesUseCase(recipesRepository)
        val getInitialRecipesUseCase = GetInitialRecipesUseCase(recipesRepository, getSortedRecipesUseCase)
        val toggleRecipesUseCase = ToggleFavoriteRecipeUseCase(recipesRepository, getSortedRecipesUseCase)

        return RecipesViewModel(getInitialRecipesUseCase, toggleRecipesUseCase, dispatcher)
    }
}