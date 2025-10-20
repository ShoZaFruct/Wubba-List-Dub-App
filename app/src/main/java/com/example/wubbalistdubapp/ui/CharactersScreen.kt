package com.example.wubbalistdubapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wubbalistdubapp.domain.model.Character

@Composable
fun CharactersRoute(
    onItemClick: (Character) -> Unit,
    vm: CharactersViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsState()
    val query by vm.query.collectAsState()

    LaunchedEffect(Unit) { vm.loadFirstPage() }

    CharactersScreen(
        state = state,
        query = query,
        onQueryChange = vm::setQuery,
        onSearch = vm::loadFirstPage,
        onLoadMore = vm::loadNextPage,
        onRetry = vm::loadFirstPage,
        onItemClick = onItemClick
    )
}

@Composable
fun CharactersScreen(
    state: UiState<List<Character>>,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    onItemClick: (Character) -> Unit
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
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Поиск по имени") }
            )
            Button(onClick = onSearch) { Text("Найти") }
        }

        when (state) {
            UiState.Idle, UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Column(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Ошибка: ${state.message}")
                Spacer(Modifier.height(12.dp))
                Button(onClick = onRetry) { Text("Повторить") }
            }
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
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
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
