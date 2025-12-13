package com.marcingantkowski.tolkienquizapp.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.marcingantkowski.tolkienquizapp.domain.model.Question

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuizScreen(quizViewModel: QuizViewModel = hiltViewModel()) {
    val state = quizViewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            ErrorState(
                errorMessage = state.error,
                onRetry = { quizViewModel.onRestartClicked() }
            )
        } else if (state.isQuizFinished) {
            QuizFinishedState(
                score = state.score,
                totalQuestions = state.questions.size,
                onRestart = { quizViewModel.onRestartClicked() }
            )
        } else if (state.questions.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Progress
                val progress = (state.currentQuestionIndex + 1) / state.questions.size.toFloat()
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Pytanie ${state.currentQuestionIndex + 1} z ${state.questions.size}",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Animated Question
                AnimatedContent(
                    targetState = state.currentQuestionIndex,
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) + fadeIn() with
                                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut()
                    }
                ) { targetIndex ->
                    val currentQuestion = state.questions[targetIndex]
                    QuestionContent(
                        question = currentQuestion,
                        selectedAnswerIndex = state.selectedAnswerIndex,
                        onAnswerSelected = { quizViewModel.onAnswerSelected(it) },
                        onNextClicked = { quizViewModel.onNextQuestionClicked() }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionContent(
    question: Question,
    selectedAnswerIndex: Int?,
    onAnswerSelected: (Int) -> Unit,
    onNextClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = question.text)
        Spacer(modifier = Modifier.height(32.dp))
        question.answers.forEachIndexed { index, answer ->
            val isSelected = selectedAnswerIndex == index
            val isCorrect = question.correctAnswerIndex == index

            val buttonColors = when {
                selectedAnswerIndex == null -> ButtonDefaults.buttonColors()
                isCorrect -> ButtonDefaults.buttonColors(containerColor = Color.Green)
                isSelected && !isCorrect -> ButtonDefaults.buttonColors(containerColor = Color.Red)
                else -> ButtonDefaults.buttonColors()
            }
            
            val icon = when {
                selectedAnswerIndex == null -> null
                isCorrect -> Icons.Default.Check
                isSelected && !isCorrect -> Icons.Default.Close
                else -> null
            }

            Button(
                onClick = { onAnswerSelected(index) },
                modifier = Modifier.fillMaxWidth(),
                colors = buttonColors,
                enabled = selectedAnswerIndex == null
            ) {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                }
                Text(text = answer)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedAnswerIndex != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onNextClicked) {
                Text(text = "Dalej")
            }
        }
    }
}

@Composable
private fun ErrorState(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Spróbuj ponownie")
        }
    }
}

@Composable
private fun QuizFinishedState(score: Int, totalQuestions: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Koniec quizu!")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Twój wynik: $score/$totalQuestions")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRestart) {
            Text(text = "Zacznij od nowa")
        }
    }
}