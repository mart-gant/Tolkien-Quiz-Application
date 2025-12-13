package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcingantkowski.tolkienquizapp.domain.use_case.GetHighScoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighScoresViewModel @Inject constructor(
    private val getHighScoresUseCase: GetHighScoresUseCase
) : ViewModel() {

    private val _state = mutableStateOf(HighScoresState())
    val state: State<HighScoresState> = _state

    init {
        loadHighScores()
    }

    private fun loadHighScores() {
        viewModelScope.launch {
            _state.value = HighScoresState(isLoading = true)
            try {
                val highScores = getHighScoresUseCase()
                _state.value = HighScoresState(highScores = highScores)
            } catch (e: Exception) {
                _state.value = HighScoresState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}