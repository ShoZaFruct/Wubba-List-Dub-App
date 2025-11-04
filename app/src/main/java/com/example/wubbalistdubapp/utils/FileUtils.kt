package com.example.wubbalistdubapp.utils

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Build


object FileUtils {

    private fun avatarsDir(context: Context): File =
        File(context.filesDir, "avatars").apply { if (!exists()) mkdirs() }

    private fun uniqueName(prefix: String = "avatar_", ext: String = ".jpg"): String {
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return "$prefix$ts$ext"
    }

    fun createImageUriInAppStorage(context: Context): Uri {
        val file = File(avatarsDir(context), uniqueName())
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun persistImageFromUri(context: Context, source: Uri): Uri {
        val resolver: ContentResolver = context.contentResolver
        val dest = File(avatarsDir(context), uniqueName())
        resolver.openInputStream(source)?.use { input ->
            FileOutputStream(dest).use { out -> input.copyTo(out) }
        }
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            dest
        )
    }

    fun deleteAvatarByUri(context: Context, uriString: String) {
        runCatching {
            if (uriString.isBlank()) return
            val uri = Uri.parse(uriString)
            val last = uri.lastPathSegment ?: return
            val name = last.substringAfterLast('/')
            val file = File(avatarsDir(context), name)
            if (file.exists()) file.delete()
        }
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun enqueueDownloadPdf(context: Context, url: String): Long {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Резюме")
            .setDescription("Загрузка файла")
            .setMimeType("application/pdf")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val fileName = "resume_${System.currentTimeMillis()}.pdf"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
        } else {
            request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
        }

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return dm.enqueue(request)
    }

    fun openDownloadedFile(context: Context, uri: Uri, mime: String = "application/pdf") {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Открыть файл"))
    }
}
