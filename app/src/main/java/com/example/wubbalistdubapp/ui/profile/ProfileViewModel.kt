package com.example.wubbalistdubapp.ui.profile

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.di.ServiceLocator
import com.example.wubbalistdubapp.domain.model.Profile
import com.example.wubbalistdubapp.domain.repository.ProfileRepository
import com.example.wubbalistdubapp.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: ProfileRepository = ServiceLocator.profileRepository
) : ViewModel() {

    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile

    init {
        viewModelScope.launch {
            repo.profileFlow.collectLatest { _profile.value = it }
        }
    }


    fun downloadResume(context: Context) {
        val url = _profile.value.resumeUrl
        if (url.isBlank()) return

        val id = FileUtils.enqueueDownloadPdf(context, url)

        viewModelScope.launch {
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            var done = false
            while (!done) {
                val q = DownloadManager.Query().setFilterById(id)
                val c: Cursor = dm.query(q)
                if (c.moveToFirst()) {
                    when (c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            val uri = dm.getUriForDownloadedFile(id) ?: break
                            FileUtils.openDownloadedFile(context, uri)
                            done = true
                        }
                        DownloadManager.STATUS_FAILED -> {
                            done = true
                        }
                    }
                }
                c.close()
                if (!done) kotlinx.coroutines.delay(500)
            }
        }
    }
}
