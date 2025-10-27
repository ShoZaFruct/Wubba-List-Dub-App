package com.example.wubbalistdubapp.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.data.local.favorites.FavoriteCharacterEntity
import com.example.wubbalistdubapp.data.mapper.toDomain
import com.example.wubbalistdubapp.di.ServiceLocator
import com.example.wubbalistdubapp.domain.model.Character
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val getByIdNet = ServiceLocator.getCharacterById
    private val favRepo = ServiceLocator.favoritesRepository

    private val _state = kotlinx.coroutines.flow.MutableStateFlow<UiState<Character>>(UiState.Loading)
    val state: kotlinx.coroutines.flow.StateFlow<UiState<Character>> = _state

    private val _isFavorite = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isFavorite: kotlinx.coroutines.flow.StateFlow<Boolean> = _isFavorite

    private val handler = CoroutineExceptionHandler { _, e ->
        if (_state.value !is UiState.Success) {
            _state.value = UiState.Error(e.localizedMessage ?: "Ошибка загрузки")
        }
    }

    private val id: Int = savedStateHandle["id"] ?: error("id is required")

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch(handler) {
            val local = favRepo.getById(id)
            if (local != null) {
                _state.value = UiState.Success(local.toDomain())
                _isFavorite.value = true
            } else {
                _state.value = UiState.Loading
                _isFavorite.value = false
            }

            try {
                val ch = getByIdNet(id)
                _state.value = UiState.Success(ch)
                _isFavorite.value = favRepo.isFavorite(id)
            } catch (ignored: Exception) {
                if (local == null) throw ignored
            }
        }
    }

    fun toggleFavorite() {
        val ch = (state.value as? UiState.Success)?.data ?: return
        viewModelScope.launch {
            if (isFavorite.value) {
                favRepo.removeById(ch.id)
                _isFavorite.value = false
            } else {
                favRepo.add(
                    FavoriteCharacterEntity(
                        id = ch.id, name = ch.name, status = ch.status,
                        species = ch.species, gender = ch.gender,
                        image = ch.image, origin = ch.origin, location = ch.location
                    )
                )
                _isFavorite.value = true
            }
        }
    }
}
