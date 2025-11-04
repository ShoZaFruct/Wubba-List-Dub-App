package com.example.wubbalistdubapp.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wubbalistdubapp.ui.components.Avatar
import com.example.wubbalistdubapp.utils.FileUtils
import kotlinx.coroutines.launch

@Composable
fun EditProfileRoute(
    onBack: () -> Unit,
    vm: EditProfileViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    EditProfileScreen(
        fullName = state.fullName,
        title = state.title,
        avatarUri = state.avatarUri,
        resumeUrl = state.resumeUrl,
        onBack = onBack,
        onChangeName = { vm.update(fullName = it) },
        onChangeTitle = { vm.update(title = it) },
        onChangeResume = { vm.update(resumeUrl = it) },
        onChangeAvatar = { vm.update(avatarUri = it) },
        onSave = { vm.save(onBack) }
    )
}

@Composable
fun EditProfileScreen(
    fullName: String,
    title: String,
    avatarUri: String,
    resumeUrl: String,
    onBack: () -> Unit,
    onChangeName: (String) -> Unit,
    onChangeTitle: (String) -> Unit,
    onChangeResume: (String) -> Unit,
    onChangeAvatar: (String) -> Unit,
    onSave: () -> Unit
) {
    val ctx = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val storagePermission =
        if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE

    var askedOnce by remember { mutableStateOf(false) }
    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) onBack()
    }

    LaunchedEffect(Unit) {
        if (!askedOnce) {
            askedOnce = true
            val granted = ContextCompat.checkSelfPermission(ctx, storagePermission) ==
                    PackageManager.PERMISSION_GRANTED
            if (!granted) storagePermissionLauncher.launch(storagePermission)
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val persisted = FileUtils.persistImageFromUri(ctx, it)
            onChangeAvatar(persisted.toString())
        }
    }

    val openDocument = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val persisted = FileUtils.persistImageFromUri(ctx, it)
            onChangeAvatar(persisted.toString())
        }
    }

    fun pickFromGallery() {
        if (Build.VERSION.SDK_INT >= 33) {
            photoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            openDocument.launch(arrayOf("image/*"))
        }
    }

    var pendingCameraUri by remember { mutableStateOf(Uri.EMPTY) }

    val takePicture = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingCameraUri != Uri.EMPTY) {
            onChangeAvatar(pendingCameraUri.toString())
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val dest = FileUtils.createImageUriInAppStorage(ctx)
            pendingCameraUri = dest
            takePicture.launch(dest)
        } else {
            scope.launch {
                val res = snackbar.showSnackbar(
                    message = "Доступ к камере отклонён",
                    actionLabel = "Настройки",
                    withDismissAction = true
                )
                if (res == SnackbarResult.ActionPerformed) {
                    FileUtils.openAppSettings(ctx)
                }
            }
        }
    }

    fun captureFromCamera() {
        val granted = ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            val dest = FileUtils.createImageUriInAppStorage(ctx)
            pendingCameraUri = dest
            takePicture.launch(dest)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var showPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                    TextButton(onClick = onSave) { Text("Готово") }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar(
                uri = avatarUri,
                size = 120.dp,
                onClick = { showPicker = true }
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = onChangeName,
                label = { Text("ФИО") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = title,
                onValueChange = onChangeTitle,
                label = { Text("Должность (опц.)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = resumeUrl,
                onValueChange = onChangeResume,
                label = { Text("URL резюме (pdf)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showPicker) {
        AlertDialog(
            onDismissRequest = { showPicker = false },
            title = { Text("Выбрать изображение") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            showPicker = false
                            pickFromGallery()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Галерея") }

                    Button(
                        onClick = {
                            showPicker = false
                            captureFromCamera()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Камера") }

                    OutlinedButton(
                        enabled = avatarUri.isNotBlank(),
                        onClick = {
                            showPicker = false
                            FileUtils.deleteAvatarByUri(ctx, avatarUri)
                            onChangeAvatar("")
                            scope.launch { snackbar.showSnackbar("Аватар удалён") }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Удалить аватар") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPicker = false }) { Text("Отмена") }
            }
        )
    }
}
