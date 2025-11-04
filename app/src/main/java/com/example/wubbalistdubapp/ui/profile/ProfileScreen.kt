package com.example.wubbalistdubapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wubbalistdubapp.domain.model.Profile
import com.example.wubbalistdubapp.ui.components.Avatar

@Composable
fun ProfileRoute(
    onEdit: () -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val profile by vm.profile.collectAsState()
    val ctx = androidx.compose.ui.platform.LocalContext.current

    ProfileScreen(
        profile = profile,
        onResumeClick = { vm.downloadResume(ctx) },
        onEdit = onEdit
    )
}

@Composable
fun ProfileScreen(
    profile: Profile,
    onResumeClick: () -> Unit,
    onEdit: () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Профиль", style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Редактировать")
                    }
                }
            }
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Avatar(uri = profile.avatarUri, size = 120.dp, onClick = {})

            Text(
                profile.fullName.ifBlank { "Имя не указано" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            if (profile.title.isNotBlank()) {
                Text(profile.title, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(8.dp))
            Button(onClick = onResumeClick, enabled = profile.resumeUrl.isNotBlank()) {
                Text("Резюме")
            }
        }
    }
}
