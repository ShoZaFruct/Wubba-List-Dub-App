package com.example.wubbalistdubapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wubbalistdubapp.data.Character

@Composable
fun CharacterDetailsScreen(
    character: Character,
    onBack: () -> Unit
) {
    Column {
        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
                Text(
                    text = "Детали",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Контент
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier.size(120.dp)
                )
                Column(Modifier.weight(1f)) {
                    Text(
                        character.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
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

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Комментарии", style = MaterialTheme.typography.titleMedium)
                    Text("Заглушка: добавим позже.")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(title: String, value: String) {
    Row {
        Text(title, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
        Text(value, Modifier.weight(1f))
    }
}
