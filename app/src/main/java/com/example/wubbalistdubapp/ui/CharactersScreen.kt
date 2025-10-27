package com.example.wubbalistdubapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.model.Filters
import com.example.wubbalistdubapp.ui.components.ErrorState

@Composable
fun CharactersRoute(
    onItemClick: (Character) -> Unit,
    onOpenFilters: () -> Unit,
    vm: CharactersViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val filters by vm.filters.collectAsState()

    CharactersScreen(
        state = state,
        filters = filters,
        onSearch = vm::loadFirstPage,
        onLoadMore = vm::loadNextPage,
        onItemClick = onItemClick,
        onOpenFilters = onOpenFilters
    )
}

@Composable
fun CharactersScreen(
    state: UiState<List<Character>>,
    filters: Filters,
    onSearch: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClick: (Character) -> Unit,
    onOpenFilters: () -> Unit
) {
    Column {
        Surface(tonalElevation = 2.dp, shadowElevation = 4.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) { Text("Персонажи", style = MaterialTheme.typography.titleLarge) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onOpenFilters) { Text("Фильтры") }
            Button(onClick = onSearch) { Text("Найти") }
        }

        when (state) {
            UiState.Idle, UiState.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> ErrorState(state.message, onSearch)
            is UiState.Success -> {
                val items = state.data
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items, key = { it.id }) { ch -> CharacterItem(ch) { onItemClick(ch) } }
                    item {
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onLoadMore,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) { Text("Ещё") }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterItem(item: Character, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = item.image, contentDescription = item.name, modifier = Modifier.size(56.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Text("${item.species} • ${item.status}", style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Последнее место: ${item.location}", style = MaterialTheme.typography.bodySmall,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
