package com.example.wubbalistdubapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.data.remote.api.NetworkModule
import com.example.wubbalistdubapp.data.repository.CharactersRepositoryImpl
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.usecase.GetCharacterByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel : ViewModel() {

    private val repo = CharactersRepositoryImpl(NetworkModule.api)
    private val getById = GetCharacterByIdUseCase(repo)

    private val _state = MutableStateFlow<UiState<Character>>(UiState.Loading)
    val state: StateFlow<UiState<Character>> = _state

    fun load(id: Int) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            try {
                _state.value = UiState.Success(getById(id))
            } catch (e: Exception) {
                _state.value = UiState.Error(e.localizedMessage ?: "Ошибка загрузки")
            }
        }
    }
}
