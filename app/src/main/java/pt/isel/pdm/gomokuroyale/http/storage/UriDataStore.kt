package pt.isel.pdm.gomokuroyale.http.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import pt.isel.pdm.gomokuroyale.http.GomokuService
import pt.isel.pdm.gomokuroyale.http.domain.Recipe
import pt.isel.pdm.gomokuroyale.http.domain.UriRepository
import pt.isel.pdm.gomokuroyale.util.HttpResult

class UriDataStore(
    private val store: DataStore<Preferences>
) : UriRepository {
    override suspend fun updateRecipeLinks(recipeLinks: List<Recipe>): List<Recipe> {
        store.edit { preferences ->
            recipeLinks.forEach { recipe ->
                val key = stringPreferencesKey(recipe.rel)
                preferences[key] = recipe.href
            }
        }
        return recipeLinks
    }


//    override suspend fun updateRecipeLinks(): List<Recipe> =
//        when (val response = gomokuService.getHome()) {
//            is HttpResult.Success -> response.value.recipeLinks
//                .map { link ->
//                    Recipe(getRelName(link.rel.first()), link.href)
//                }.also { recipes ->
//                    store.edit { preferences ->
//                        recipes.forEach { recipe ->
//                            val key = stringPreferencesKey(recipe.rel)
//                            preferences[key] = recipe.href
//                        }
//                    }
//                }
//            else -> emptyList()
//        }

    override suspend fun getRecipeLink(rel: String): Recipe? =
        store.data.first().let { preferences ->
            val href = preferences[stringPreferencesKey(rel)] ?: return null
            return Recipe(rel, href)
        }
}