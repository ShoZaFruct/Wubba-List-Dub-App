package com.example.wubbalistdubapp.ui.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wubbalistdubapp.di.ServiceLocator
import com.example.wubbalistdubapp.domain.model.Filters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FiltersViewModel : ViewModel() {
    private val ds = ServiceLocator.filtersDataStore
    private val badge = ServiceLocator.filtersBadgeCache

    private val _state = MutableStateFlow(Filters())
    val state: StateFlow<Filters> = _state

    init {
        viewModelScope.launch {
            val f = ds.filtersFlow.first()
            _state.value = f
            badge.showBadge = !f.isDefault()
        }
    }

    fun setName(value: String) { _state.value = _state.value.copy(name = value) }
    fun setStatus(value: String) { _state.value = _state.value.copy(status = value) }
    fun setGender(value: String) { _state.value = _state.value.copy(gender = value) }

    fun save(onSaved: () -> Unit) {
        viewModelScope.launch {
            val f = _state.value
            ds.save(f)
            badge.showBadge = !f.isDefault()
            onSaved()
        }
    }

    fun reset(onSaved: () -> Unit) {
        viewModelScope.launch {
            val def = Filters()
            ds.save(def)
            _state.value = def
            badge.showBadge = false
            onSaved()
        }
    }
}
