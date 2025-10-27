package com.example.wubbalistdubapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.di.ServiceLocator
import com.example.wubbalistdubapp.domain.model.Character
import com.example.wubbalistdubapp.domain.model.Filters
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {

    private val getCharacters = ServiceLocator.getCharacters
    private val filtersFlow = ServiceLocator.filtersDataStore.filtersFlow

    private val _state = MutableStateFlow<UiState<List<Character>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Character>>> = _state

    private val _filters = MutableStateFlow(Filters())
    val filters: StateFlow<Filters> = _filters

    private var currentPage = 1
    private var job: Job? = null

    private val handler = CoroutineExceptionHandler { _, e ->
        _state.value = UiState.Error(e.localizedMessage ?: "Ошибка загрузки")
    }

    init {
        viewModelScope.launch {
            filtersFlow.collectLatest { f ->
                _filters.value = f
                loadFirstPage()
            }
        }
    }

    fun loadFirstPage() {
        currentPage = 1
        request(page = currentPage, replace = true)
    }

    fun loadNextPage() {
        request(page = currentPage + 1, replace = false)
    }

    private fun request(page: Int?, replace: Boolean) {
        job?.cancel()
        job = viewModelScope.launch(handler) {
            if (replace) _state.value = UiState.Loading
            val f = _filters.value
            val items = getCharacters(
                page = page,
                name = f.name.ifBlank { null }
            ).filter { ch ->
                (f.status == "any" || ch.status.equals(f.status, true)) &&
                        (f.gender == "any" || ch.gender.equals(f.gender, true))
            }

            if (replace) {
                _state.value = UiState.Success(items)
            } else {
                val old = (state.value as? UiState.Success)?.data.orEmpty()
                _state.value = UiState.Success(old + items)
            }
            if (page != null) currentPage = page
        }
    }
}
