package com.example.wubbalistdubapp.ui.filters

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FiltersRoute(onDone: () -> Unit, vm: FiltersViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    FiltersScreen(
        name = state.name,
        status = state.status,
        gender = state.gender,
        onNameChange = vm::setName,
        onStatusChange = vm::setStatus,
        onGenderChange = vm::setGender,
        onDone = { vm.save(onDone) },
        onReset = { vm.reset(onDone) }
    )
}

@Composable
fun FiltersScreen(
    name: String,
    status: String,
    gender: String,
    onNameChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onDone: () -> Unit,
    onReset: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Фильтры", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Имя (name)") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownField(
            label = "Статус",
            value = status,
            values = listOf("any", "alive", "dead", "unknown"),
            onSelect = onStatusChange
        )
        DropdownField(
            label = "Пол",
            value = gender,
            values = listOf("any", "female", "male", "genderless", "unknown"),
            onSelect = onGenderChange
        )

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onDone, modifier = Modifier.weight(1f)) { Text("Готово") }
            OutlinedButton(onClick = onReset, modifier = Modifier.weight(1f)) { Text("Сбросить") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    value: String,
    values: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)  
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            values.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = { onSelect(it); expanded = false }
                )
            }
        }
    }
}
