package pt.isel.pdm.gomokuroyale.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.gomokuroyale.R
import pt.isel.pdm.gomokuroyale.rankings.ui.SearchBarTestTag
import pt.isel.pdm.gomokuroyale.ui.theme.AlabasterWhite
import pt.isel.pdm.gomokuroyale.ui.theme.Brown

private const val MIN_QUERY_LENGTH = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    query: String,
    onSearchRequested: (String) -> Unit = { },
    onQueryChanged: (String) -> Unit = { },
    onClearSearch: () -> Unit = { },
){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        SearchBar(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .testTag(SearchBarTestTag),
            placeholder = {
                Text(
                    stringResource(R.string.search_hint),
                    textAlign = TextAlign.Center,
                )
            },
            query = query,
            onQueryChange = { onQueryChanged(it) },
            onSearch = { if(query.length >= MIN_QUERY_LENGTH) onSearchRequested(it)  },
            active = false,
            onActiveChange = { },
            leadingIcon = {
                Image(
                    painterResource(id = R.drawable.search),
                    contentDescription = null
                )
            },
            enabled = true,
            trailingIcon = {
                Image(
                    painterResource(id = R.drawable.cancel_icon),
                    contentDescription = null,
                    modifier = Modifier.clickable { onClearSearch() }
                )
            },
            content = { },
            colors = SearchBarDefaults.colors(
                containerColor = Brown,
                dividerColor = AlabasterWhite,
            )
        )
    }
}

@Preview
@Composable
fun SearchBarPreview(){
    MySearchBar(
        query = "",
        onSearchRequested = { },
        onQueryChanged = { },
        onClearSearch = { },
    )
}