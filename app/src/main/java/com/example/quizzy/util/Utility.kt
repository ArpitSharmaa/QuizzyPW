package com.example.quizzy.util

import android.app.Activity
import android.os.Build
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import com.example.quizzy.ui.theme.BlueBubble
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object Utility {
    private val _errorChannel = Channel<String>()
    val errorFlow = _errorChannel.receiveAsFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorChannel.trySend(throwable.message ?: "Unknown Error")
        throwable.printStackTrace()
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFEAEAEA),
                Color(0xFFB8B5B5),
                Color(0xFFEAEAEA),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
fun Modifier.interaction(onClick: () -> Unit): Modifier {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    return this
        .background(
            color = if (isPressed) BlueBubble.copy(0.2f) else Color.Transparent,
            shape = RoundedCornerShape(5.dp)
        )
        .padding(5.dp, 5.dp)
        .clickable(interactionSource = interactionSource, indication = null) {
            onClick.invoke()
        }
}

@Composable
fun Modifier.interactionWithoutPadding(onClick: () -> Unit): Modifier {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    return this
        .background(
            color = if (isPressed) BlueBubble.copy(0.2f) else Color.Transparent,
            shape = RoundedCornerShape(5.dp)
        )
        .clickable(interactionSource = interactionSource, indication = null) {
            onClick.invoke()
        }
}

@Composable
fun SetStatusBarColor(
    color: Color = Color.Transparent,
    useDarkIcons: Boolean
) {
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
                window.decorView.setBackgroundColor(color.toArgb())

            } else {
                // For Android 14 and below
                window.statusBarColor = color.toArgb()
            }


            val insetsController = WindowInsetsControllerCompat(window, view)
            insetsController.isAppearanceLightStatusBars = useDarkIcons
        }
    }
}