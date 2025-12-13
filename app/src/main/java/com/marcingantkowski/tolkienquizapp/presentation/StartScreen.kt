package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.marcingantkowski.tolkienquizapp.presentation.navigation.Screen

val difficulties = listOf("Any", "Easy", "Medium", "Hard")
val types = mapOf("Any" to "any", "Multiple Choice" to "multiple", "True / False" to "boolean")

@Composable
fun StartScreen(
    navController: NavController,
    startViewModel: StartViewModel = hiltViewModel()
) {
    val state = startViewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Text(text = state.error, modifier = Modifier.align(Alignment.Center))
        } else {
            val categories = remember(state.categories) {
                mapOf("Any Category" to -1) + state.categories.associate { it.name to it.id }
            }
            StartScreenContent(navController, categories)
        }
    }
}

@Composable
private fun StartScreenContent(
    navController: NavController,
    categories: Map<String, Int>
) {
    var selectedCategory by remember { mutableStateOf("Any Category") }
    var selectedDifficulty by remember { mutableStateOf("Any") }
    var selectedType by remember { mutableStateOf("Any") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tolkien Quiz App", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(48.dp))

        // Category Dropdown
        Box {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                label = { Text("Category") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { categoryExpanded = true }
            )
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.keys.forEach { category ->
                    DropdownMenuItem(text = { Text(category) }, onClick = {
                        selectedCategory = category
                        categoryExpanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Difficulty Dropdown
        Box {
            OutlinedTextField(
                value = selectedDifficulty,
                onValueChange = {},
                label = { Text("Difficulty") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { difficultyExpanded = true }
            )
            DropdownMenu(
                expanded = difficultyExpanded,
                onDismissRequest = { difficultyExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                difficulties.forEach { difficulty ->
                    DropdownMenuItem(text = { Text(difficulty) }, onClick = {
                        selectedDifficulty = difficulty
                        difficultyExpanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Type Dropdown
        Box {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                label = { Text("Type") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { typeExpanded = true }
            )
            DropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                types.keys.forEach { type ->
                    DropdownMenuItem(text = { Text(type) }, onClick = {
                        selectedType = type
                        typeExpanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            val categoryId = categories[selectedCategory]
            val difficultyValue = selectedDifficulty.lowercase()
            val typeValue = types[selectedType]
            navController.navigate(Screen.QuizScreen.createRoute(categoryId, difficultyValue, typeValue))
        }) {
            Text(text = "Rozpocznij Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate(Screen.HighScoresScreen.route) }) {
            Text(text = "High Scores")
        }
    }
}