package com.example.wubbalistdubapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.data.remote.api.NetworkModule
import com.example.wubbalistdubapp.data.repository.CharactersRepositoryImpl
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    private val repo = CharactersRepositoryImpl(NetworkModule.api)
    private val getCharacters = GetCharactersUseCase(repo)

    private val _state = MutableStateFlow<UiState<List<Character>>>(UiState.Idle)
    val state: StateFlow<UiState<List<Character>>> = _state

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private var currentPage = 1
    private var job: Job? = null

    fun setQuery(q: String) { _query.value = q }

    fun loadFirstPage() {
        currentPage = 1
        request(page = currentPage, name = query.value.ifBlank { null }, replace = true)
    }

    fun loadNextPage() {
        request(page = currentPage + 1, name = query.value.ifBlank { null }, replace = false)
    }

    private fun request(page: Int?, name: String?, replace: Boolean) {
        job?.cancel()
        job = viewModelScope.launch {
            if (replace) _state.value = UiState.Loading
            try {
                val data = getCharacters(page, name)
                if (replace) {
                    _state.value = UiState.Success(data)
                } else {
                    val old = (state.value as? UiState.Success)?.data.orEmpty()
                    _state.value = UiState.Success(old + data)
                }
                if (page != null) currentPage = page
            } catch (e: Exception) {
                _state.value = UiState.Error(e.localizedMessage ?: "Ошибка загрузки")
            }
        }
    }
}
