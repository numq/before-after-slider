import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "before-after slider animation",
    state = WindowState(width = 500.dp, height = 500.dp)
) {
    val percentage = remember { Animatable(0f) }

    val (orientation, setOrientation) = remember { mutableStateOf(BeforeAfterSliderOrientation.HORIZONTAL) }

    val linearAnimation = remember { tween<Float>(1_000, delayMillis = 500) }

    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        percentage.animateTo(1f, animationSpec = linearAnimation)

        setOrientation(BeforeAfterSliderOrientation.VERTICAL)

        percentage.animateTo(0f, animationSpec = linearAnimation)

        setOrientation(BeforeAfterSliderOrientation.HORIZONTAL)

        isAnimating = false
    }

    BeforeAfterSlider(
        modifier = Modifier.fillMaxSize(),
        orientation = orientation,
        defaultPercentage = percentage.value,
        overriddenPercentage = if (isAnimating) percentage.value else null
    )
}