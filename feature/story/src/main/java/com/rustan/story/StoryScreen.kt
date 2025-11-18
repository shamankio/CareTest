package com.rustan.story

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rustan.domain.entities.Story
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoryScreenView(
    storyViewModel: StoryViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    city: String
) {
    LaunchedEffect(Unit) {
        storyViewModel.loadStories(city)
    }

    val state by storyViewModel.storyScreenState.collectAsStateWithLifecycle()
    when (state) {
        is StoryScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is StoryScreenState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val message = when ((state as StoryScreenState.Error).error) {
                    StoryError.Api -> "API error"
                    StoryError.NetworkFailure -> "Network failure"
                    StoryError.Unknown -> "Unknown error"
                }
                Text(
                    text = message,
                    color = Color.Red
                )
            }
        }

        is StoryScreenState.Success -> {
            StoryScreen(
                viewState = (state as StoryScreenState.Success).storyViewState,
                onEvent = storyViewModel::onEvent,
                onNavigateBack = onNavigateBack,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

@Composable
fun StoryScreen(
    viewState: StoryViewState,
    onEvent: (StoryEvent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    Box(modifier = modifier) {
        if (viewState.currentIndex in viewState.stories.indices) {
            val story = viewState.stories[viewState.currentIndex]

            AsyncImage(
                model = story.imageURL,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                            },
                            onDragEnd = {
                                if (offsetX > 50) {
                                    onEvent(StoryEvent.Previous)
                                } else if (offsetX < -50) {
                                    onEvent(StoryEvent.Next)
                                }
                                offsetX = 0f
                            }
                        )
                    },
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.safeGestures)
            ) {
                ProgressBars(
                    stories = viewState.stories,
                    currentIndex = viewState.currentIndex,
                    progress = viewState.progress.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )

                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressBars(
    stories: List<Story>,
    currentIndex: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        stories.indices.forEach { index ->
            ProgressBarSegment(
                index = index,
                currentIndex = currentIndex,
                progress = progress,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = true)
@Composable
fun ProgressBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
    ) {
        ProgressBars(
            stories = listOf(
                Story("1"),
                Story("2"),
                Story("3"),
                Story("4"),
                Story("5")
            ),
            currentIndex = 1,
            progress = 0.7f
        )
    }
}

@Composable
fun ProgressBarSegment(
    index: Int,
    currentIndex: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(4.dp) // Made the progress bar thinner for a more standard look
    ) {
        // Background capsule
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.3f)) // Using a more standard subdued color
        )

        // Foreground progress
        if (index == currentIndex) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = progress)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
                    )
            )
        } else if (index < currentIndex) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            )
        }
    }
}

@Preview
@Composable
fun ProgressBarSegmentPreview() {
    Box(
        modifier = Modifier
            .width(104.dp)
            .background(Color.DarkGray)
    ) {
        ProgressBarSegment(
            index = 0,
            currentIndex = 0,
            progress = 0.5f,
        )
    }
}
