package com.marcingantkowski.tolkienquizapp.presentation

import com.marcingantkowski.tolkienquizapp.domain.model.Category

data class StartScreenState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)