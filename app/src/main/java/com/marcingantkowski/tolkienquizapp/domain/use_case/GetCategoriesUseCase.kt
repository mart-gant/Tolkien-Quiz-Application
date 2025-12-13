package com.marcingantkowski.tolkienquizapp.domain.use_case

import com.marcingantkowski.tolkienquizapp.domain.model.Category
import com.marcingantkowski.tolkienquizapp.domain.repository.QuizRepository

class GetCategoriesUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}