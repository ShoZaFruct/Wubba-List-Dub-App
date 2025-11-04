package com.example.wubbalistdubapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.di.ServiceLocator
import com.example.wubbalistdubapp.domain.model.Profile
import com.example.wubbalistdubapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repo: ProfileRepository = ServiceLocator.profileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Profile())
    val state: StateFlow<Profile> = _state

    init {
        viewModelScope.launch {
            _state.value = repo.get()
        }
    }

    fun update(fullName: String? = null, title: String? = null, avatarUri: String? = null, resumeUrl: String? = null) {
        _state.value = _state.value.copy(
            fullName = fullName ?: _state.value.fullName,
            title = title ?: _state.value.title,
            avatarUri = avatarUri ?: _state.value.avatarUri,
            resumeUrl = resumeUrl ?: _state.value.resumeUrl
        )
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.save(_state.value)
            onDone()
        }
    }
}
