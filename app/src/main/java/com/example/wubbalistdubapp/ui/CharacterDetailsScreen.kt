package com.example.wubbalistdubapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.ui.components.ErrorState

@Composable
fun CharacterDetailsRoute(
    onBack: () -> Unit,
    vm: CharacterDetailsViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    val isFav by vm.isFavorite.collectAsState()

    CharacterDetailsScreen(
        state = state,
        isFavorite = isFav,
        onToggleFavorite = vm::toggleFavorite,
        onBack = onBack
    )
}

@Composable
fun CharacterDetailsScreen(
    state: UiState<Character>,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Column {
        Surface(tonalElevation = 2.dp, shadowElevation = 4.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад") }
                Text("Детали", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 4.dp))
            }
        }

        when (state) {
            UiState.Idle, UiState.Loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is UiState.Error -> ErrorState(message = state.message, onRetry = onBack)
            is UiState.Success -> DetailsContent(state.data, isFavorite, onToggleFavorite)
        }
    }
}

@Composable
private fun DetailsContent(character: Character, isFavorite: Boolean, onToggleFavorite: () -> Unit) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(model = character.image, contentDescription = character.name, modifier = Modifier.size(120.dp))
            Column(Modifier.weight(1f)) {
                Text(character.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text(character.status) })
                    AssistChip(onClick = {}, label = { Text(character.species) })
                    AssistChip(onClick = {}, label = { Text(character.gender) })
                }
                Spacer(Modifier.height(8.dp))
                Text("Origin: ${character.origin}", style = MaterialTheme.typography.bodyMedium)
                Text("Location: ${character.location}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Button(onClick = onToggleFavorite) {
            Text(if (isFavorite) "Удалить из избранного" else "В избранное")
        }

        Card {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Информация", style = MaterialTheme.typography.titleMedium)
                Divider()
                InfoRow("Статус", character.status)
                InfoRow("Вид", character.species)
                InfoRow("Пол", character.gender)
                InfoRow("Происхождение", character.origin)
                InfoRow("Текущее местоположение", character.location)
            }
        }
    }
}

@Composable private fun InfoRow(title: String, value: String) {
    Row { Text(title, Modifier.weight(1f), fontWeight = FontWeight.SemiBold); Text(value, Modifier.weight(1f)) }
}
