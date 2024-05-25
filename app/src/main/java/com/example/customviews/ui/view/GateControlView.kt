package com.example.customviews.ui.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GateControlView() {
    val gate1State = rememberGateState()
    val gate2State = rememberGateState()
    val gate3State = rememberGateState()
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    gate1State.direction = GateState.Direction.NIL
                    gate2State.direction = GateState.Direction.NIL
                    gate3State.direction = GateState.Direction.NIL
                }) {
                Text(text = "Reset")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 120.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Lift to 30/F - 60/F", textAlign = TextAlign.Center, modifier = Modifier)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Gate(
                            state = gate1State,
                            modifier = Modifier
                                .width(72.dp)
                                .height(240.dp),
                            arrowWidth = 42.dp
                        )
                    }
                    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Gate(
                            state = gate2State,
                            modifier = Modifier
                                .width(108.dp)
                                .height(240.dp),
                            arrowWidth = 42.dp
                        )
                    }
                    Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Gate(
                            state = gate3State,
                            modifier = Modifier
                                .width(72.dp)
                                .height(240.dp),
                            arrowWidth = 42.dp
                        )
                    }
                }
            }
        }
    }
}

class GateState {
    enum class Direction {
        UPWARDS,
        DOWNWARDS,
        NIL
    }
    var direction by mutableStateOf<Direction>(Direction.NIL)
}

@Composable
fun rememberGateState() = remember { GateState() }

@Composable
fun Gate(
    modifier: Modifier = Modifier,
    arrowWidth: Dp,
    state: GateState = rememberGateState(),
) {
    val transition = updateTransition(targetState = state.direction, label = "")
    val cardBg by transition.animateColor(label = "") {
        if (it == GateState.Direction.NIL) MaterialTheme.colorScheme.surfaceVariant else Color.hsl(85f, .6f, .2f)
    }
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        AnimatedVisibility(visible = state.direction != GateState.Direction.UPWARDS, modifier = Modifier.weight(.5f)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clickable { state.direction = GateState.Direction.DOWNWARDS },
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxSize()
                        .rotate(180f),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ArrowSet(
                        arrowWidth = arrowWidth,
                        isPlaying = state.direction == GateState.Direction.DOWNWARDS
                    )
                }
            }
        }
        AnimatedVisibility(visible = state.direction != GateState.Direction.DOWNWARDS, modifier = Modifier.weight(.5f)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .clickable { state.direction = GateState.Direction.UPWARDS },
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxSize(),
                ) {
                    ArrowSet(arrowWidth = arrowWidth, isPlaying = state.direction == GateState.Direction.UPWARDS)
                }
            }
        }
    }
}

@Composable
fun ArrowSet(
    isPlaying: Boolean = false,
    arrowWidth: Dp,
) {
    AnimatedVisibility(visible = isPlaying, enter = fadeIn(), exit = fadeOut()) {
        val transition = rememberInfiniteTransition(label = "")
        val alpha1 by transition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                keyframes {
                    durationMillis = 1500
                    0f at 0 with EaseInOutSine
                    0f at 500 with EaseInOutSine
                    1f at 1000 with EaseInOutSine
                    0f at 1500 with EaseInOutSine
                },
                repeatMode = RepeatMode.Restart),
            label = ""
        )
        val alpha2 by transition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                keyframes {
                    durationMillis = 1500
                    0f at 0 with EaseInOutSine
                    1f at 500 with EaseInOutSine
                    0f at 1000 with EaseInOutSine
                    0f at 1500 with EaseInOutSine
                },
                repeatMode = RepeatMode.Restart),
            label = ""
        )
        val alpha3 by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                keyframes {
                    durationMillis = 1500
                    1f at 0 with EaseInOutSine
                    0f at 500 with EaseInOutSine
                    0f at 1000 with EaseInOutSine
                    1f at 1500 with EaseInOutSine
                },
                repeatMode = RepeatMode.Restart),
            label = ""
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Arrow(
                modifier = Modifier
                    .width(arrowWidth)
                    .height(arrowWidth / 2)
                    .alpha(alpha1),
                arrowAngle = .4f,
                color = Color.hsl(50f, .7f, .6f)
            )
            Arrow(
                modifier = Modifier
                    .width(arrowWidth)
                    .height(arrowWidth / 2)
                    .alpha(alpha2),
                arrowAngle = .4f,
                color = Color.hsl(50f, .7f, .6f)
            )
            Arrow(
                modifier = Modifier
                    .width(arrowWidth)
                    .height(arrowWidth / 2)
                    .alpha(alpha3),
                arrowAngle = .4f,
                color = Color.hsl(50f, .7f, .6f)
            )
        }
    }
    AnimatedVisibility(visible = !isPlaying, enter = fadeIn(), exit = fadeOut()) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Arrow(
                modifier = Modifier
                    .width(arrowWidth)
                    .height(arrowWidth / 2),
                arrowAngle = .4f
            )
        }
    }
}

@Preview
@Composable
fun Arrow(
    modifier: Modifier = Modifier,
    arrowAngle: Float = .2f,
    color: Color = Color.hsl(85f, .5f, .8f)
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path()
            path.moveTo(size.width * 0f, size.height * 1f)
            path.lineTo(size.width * .5f, size.height * (1 - arrowAngle))
            path.lineTo(size.width * 1f, size.height * 1f)
            path.lineTo(size.width * 1f, size.height * arrowAngle)
            path.lineTo(size.width * .5f, size.height * 0f)
            path.lineTo(size.width * 0f, size.height * arrowAngle)
            path.close()
            drawPath(path, color, style = Fill)
        }
    }
}