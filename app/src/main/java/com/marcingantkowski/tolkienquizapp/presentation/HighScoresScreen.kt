package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HighScoresScreen(highScoresViewModel: HighScoresViewModel = hiltViewModel()) {
    val state = highScoresViewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Text(
                text = state.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (state.highScores.isEmpty()) {
            Text(
                text = "No high scores yet!",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.highScores) { highScore ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Score: ${highScore.score}/${highScore.totalQuestions}")
                        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(highScore.timestamp))
                        Text(text = date)
                    }
                }
            }
        }
    }
}