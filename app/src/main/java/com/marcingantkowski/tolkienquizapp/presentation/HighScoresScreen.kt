package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.marcingantkowski.tolkienquizapp.R
import com.marcingantkowski.tolkienquizapp.domain.model.HighScore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighScoresScreen(
    viewModel: HighScoresViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.high_scores_title)) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.error != null -> {
                    Text(
                        text = state.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                state.highScores.isEmpty() -> {
                    Text(
                        text = stringResource(id = R.string.no_scores_available),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    HighScoresList(highScores = state.highScores)
                }
            }
        }
    }
}

@Composable
private fun HighScoresList(highScores: List<HighScore>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(highScores) { score ->
            HighScoreItem(highScore = score)
            Divider()
        }
    }
}

@Composable
private fun HighScoreItem(highScore: HighScore) {
    // Date Formatter
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = Date(highScore.timestamp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${highScore.score}/${highScore.totalQuestions}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = sdf.format(date),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}