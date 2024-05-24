package com.example.customviews.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun BigButtonView() {
    BigButton()
}

class BigButtonState {
    var on by mutableStateOf(false)
}

@Composable
fun rememberBigButtonState() = remember { BigButtonState() }

@Composable
fun BigButton(
    state: BigButtonState = rememberBigButtonState()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.on,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                with(LocalDensity.current) {
                    RadiatingWaves(
                        radius = 300.dp.toPx(),
                        numberOfWaves = 3,
                        interpolateSteps = 5,
                        exponent = 4,
                    )
                }
            }
        }
        val transition = updateTransition(targetState = state.on, label = "")
        val gridAlpha by transition.animateFloat(label = "") {
            if (it) .2f else .05f
        }
        val pillColor by transition.animateColor(label = "") {
            if (it) Color.hsl(136f, .55f, .3f) else Color.hsl(136f, .05f, .05f);
        }
        Grid(
            modifier = Modifier.align(Alignment.Center),
            gap = 36.dp,
            dividerWidth = 1.5.dp,
            dividerAlpha = gridAlpha
        )
        ElevatedButton(
            onClick = { state.on = !state.on },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .align(Alignment.Center),
            shape = CircleShape,
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 8.dp,
                focusedElevation = 20.dp,
                hoveredElevation = 20.dp,
                pressedElevation = 20.dp
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 24.dp)) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(4.dp))
                        .background(pillColor)
                        .width(84.dp)
                        .height(4.dp)
                )
                ButtonLabel(
                    on = state.on,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun RadiatingWaves(
    modifier: Modifier = Modifier,
    radius: Float,
    tween: TweenSpec<Float> = tween(1500, easing = LinearEasing),
    numberOfWaves: Int,
    interpolateSteps: Int = 5,
    exponent: Int = 3,
) {
    val transition = rememberInfiniteTransition(label = "")
    val shift by transition.animateFloat(
        initialValue = -1f / numberOfWaves, targetValue = 0f, animationSpec = infiniteRepeatable(
            tween,
            repeatMode = RepeatMode.Restart,
        ), label = ""
    )
    val steps = Array(interpolateSteps + 1) { 0f }
        .withIndex()
        .map { x -> (x.index / interpolateSteps.toFloat()) * (1f - 0.01f) }
    val colors = steps.map { pos ->
        pos to Color.hsl(
            0f,
            0f,
            lightness = pos.pow(exponent),
            alpha = .3f
        )
    } + (1f to Color.hsl(0f, 0f, lightness = 0f, alpha = 0f))
    val loopColors = Array(numberOfWaves + 1) { 0 }
        .withIndex()
        .flatMap { x -> colors.map { p -> (x.index + p.first) / numberOfWaves.toFloat() to p.second } }
    Canvas(modifier = modifier.fillMaxSize()) {
        val shiftedLoopColors = loopColors.map { pair ->
            (pair.first + shift) to pair.second
        }
        val scaledShiftedLoopColors = shiftedLoopColors.map { pair ->
            pair.first to pair.second.copy(alpha = pair.second.alpha * ((cos(pair.first * Math.PI) + 1f) / 2f).toFloat())
        }
        drawCircle(
            Brush.radialGradient(
                colorStops = scaledShiftedLoopColors.toTypedArray(),
                radius = radius
            ), radius = radius
        )
    }
}

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    gap: Dp = 20.dp,
    dividerWidth: Dp = 2.dp,
    dividerAlpha: Float = 0.2f,
) {
    val colors = arrayOf(
        0f to Color.hsl(0f, 0f, lightness = 0f, alpha = 0f),
        0.3f to Color.hsl(0f, 0f, lightness = 1f, alpha = dividerAlpha),
        0.7f to Color.hsl(0f, 0f, lightness = 1f, alpha = dividerAlpha),
        1f to Color.hsl(0f, 0f, lightness = 0f, alpha = 0f),
    )
    Canvas(modifier = modifier.fillMaxSize()) {
        val numberOfVerticalDividers = (size.width / gap.toPx()).toInt()
        val numberOfHorizontalDividers = (size.height / gap.toPx()).toInt()
        for (i in 0..numberOfHorizontalDividers) {
            drawRect(
                Brush.linearGradient(
                    colorStops = colors,
                    start = Offset.Zero,
                    end = Offset(x = size.width, y = 0f)
                ),
                size = Size(width = size.width, height = dividerWidth.toPx()),
                topLeft = Offset(x = 0f, y = i * size.height / numberOfHorizontalDividers),
                alpha = if (i * 2 == numberOfHorizontalDividers) 1f else (sin(Math.PI * (i / (numberOfHorizontalDividers / 2f) - 1f)) / (Math.PI * (i / (numberOfHorizontalDividers / 2f) - 1f))).toFloat()
            )
        }
        for (i in 0..numberOfVerticalDividers) {
            drawRect(
                Brush.linearGradient(
                    colorStops = colors,
                    start = Offset.Zero,
                    end = Offset(x = 0f, y = size.height)
                ),
                size = Size(width = dividerWidth.toPx(), height = size.height),
                topLeft = Offset(x = i * size.width / numberOfVerticalDividers, y = 0f),
                alpha = if (i * 2 == numberOfVerticalDividers) 1f else (sin(Math.PI * (i / (numberOfVerticalDividers / 2f) - 1f)) / (Math.PI * (i / (numberOfVerticalDividers / 2f) - 1f))).toFloat()
            )
        }
    }
}

@Composable
fun ButtonLabel(
    on: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = !on,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 32 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -32 }),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "OFF",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.hsl(0f, 0f, .7f),
            )
        }
        AnimatedVisibility(
            visible = on,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 32 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -32 }),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Broadcasting...",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.hsl(0f, 0f, .7f),
            )
        }
    }
}