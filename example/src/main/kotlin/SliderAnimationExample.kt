import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.delay

fun main() = singleWindowApplication(
    title = "before-after slider animation",
    state = WindowState(width = 500.dp, height = 500.dp)
) {
    val percentage = remember { Animatable(0.5f) }

    val (orientation, setOrientation) = remember { mutableStateOf(BeforeAfterSliderOrientation.HORIZONTAL) }

    val linearAnimation = remember { tween<Float>(1_000, delayMillis = 500) }

    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        percentage.animateTo(.75f, animationSpec = linearAnimation)

        percentage.animateTo(.25f, animationSpec = linearAnimation)

        delay(500L)

        setOrientation(BeforeAfterSliderOrientation.VERTICAL)

        delay(500L)

        percentage.animateTo(1f, animationSpec = linearAnimation)

        percentage.animateTo(.25f, animationSpec = linearAnimation)

        isAnimating = false
    }

    BeforeAfterSliderAccessible(
        modifier = Modifier.fillMaxSize(),
        orientation = orientation,
        enabled = !isAnimating,
        defaultPercentage = percentage.value,
        overriddenPercentage = if (isAnimating) percentage.value else null
    )
}