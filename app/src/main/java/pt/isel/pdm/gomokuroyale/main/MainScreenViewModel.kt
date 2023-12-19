package pt.isel.pdm.gomokuroyale.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.http.storage.UriDataStore
import pt.isel.pdm.gomokuroyale.util.IOState
import pt.isel.pdm.gomokuroyale.util.idle
import pt.isel.pdm.gomokuroyale.util.loading
import pt.isel.pdm.gomokuroyale.util.onFailureResult
import pt.isel.pdm.gomokuroyale.util.onSuccessResult
import pt.isel.pdm.gomokuroyale.util.saveFailure
import pt.isel.pdm.gomokuroyale.util.saved

class MainScreenViewModel(
    private val uriRepository: UriRepository,
    private val gomokuService: GomokuService
) : ViewModel() {

    private val _state = MutableStateFlow<IOState<List<Recipe>>>(idle())
    val state = _state.asStateFlow()

    fun updateRecipes() {
        _state.value = loading()
        viewModelScope.launch {
            val response = gomokuService.getHome()
            response.onSuccessResult {
                val recipes = it.map { link ->
                    Recipe(getRelName(link.rel.first()), link.href)
                }
                val result =
                    kotlin.runCatching { uriRepository.updateRecipeLinks(recipes); recipes }
                _state.value = saved(result)
            }.onFailureResult {
                _state.value = saveFailure(it)
            }
        }
    }

    private fun getRelName(relUrl: String) = relUrl.split(DELIMITER).last()

    companion object {
        fun factory(gomokuService: GomokuService, uriRepository: UriRepository) = viewModelFactory {
            initializer { MainScreenViewModel(uriRepository, gomokuService) }
        }

        private const val DELIMITER = "/"
    }
}