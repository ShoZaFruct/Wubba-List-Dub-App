package com.example.wubbalistdubapp.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity

@Composable
fun FavoritesRoute(
    onItemClick: (Int) -> Unit,
    vm: FavoritesViewModel = viewModel()
) {
    val items by vm.favorites.collectAsState()
    FavoritesScreen(items, onItemClick)
}

@Composable
fun FavoritesScreen(
    items: List<FavoriteCharacterEntity>,
    onItemClick: (Int) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Surface(tonalElevation = 2.dp, shadowElevation = 4.dp) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) { Text("Избранное", style = MaterialTheme.typography.titleLarge) }
        }
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items, key = { it.id }) { it ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(it.id) }
                ) {
                    Row(
                        Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = it.image,
                            contentDescription = it.name,
                            modifier = Modifier.size(56.dp)
                        )
                        Column {
                            Text(it.name, style = MaterialTheme.typography.titleMedium)
                            Text("${it.species} • ${it.status}")
                            Text("Место: ${it.location}")
                        }
                    }
                }
            }
        }
    }
}
