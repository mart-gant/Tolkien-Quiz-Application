package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = mutableStateOf(StartScreenState())
    val state: State<StartScreenState> = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.value = StartScreenState(isLoading = true)
            try {
                val categories = getCategoriesUseCase()
                _state.value = StartScreenState(categories = categories)
            } catch (e: Exception) {
                _state.value = StartScreenState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}