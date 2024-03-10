import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun BeforeAfter(
    modifier: Modifier,
    orientation: BeforeAfterSlider.Orientation,
    contentBefore: @Composable BoxWithConstraintsScope.() -> Unit,
    contentAfter: @Composable BoxWithConstraintsScope.() -> Unit,
    sliderModifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.padding(4.dp)) {

        var previousMaxWidth by remember { mutableStateOf(0f) }

        var previousMaxHeight by remember { mutableStateOf(0f) }

        var offsetX by remember(orientation) { mutableStateOf(maxWidth.div(2).value) }

        var offsetY by remember(orientation) { mutableStateOf(maxHeight.div(2).value) }

        val sliderOffsetX = with(LocalDensity.current) {
            offsetX.toDp()
        }

        val sliderOffsetY = with(LocalDensity.current) {
            offsetY.toDp()
        }

        val draggingModifier = Modifier.pointerInput(orientation, maxWidth, maxHeight) {
            detectDragGestures { change, (x, y) ->
                change.consume()
                when (orientation) {
                    BeforeAfterSlider.Orientation.HORIZONTAL -> offsetX = (offsetX + x).coerceIn(0f, maxWidth.value)

                    BeforeAfterSlider.Orientation.VERTICAL -> offsetY = (offsetY + y).coerceIn(0f, maxHeight.value)
                }
            }
        }

        val sliderOffsetModifier = Modifier.offset(
            x = if (orientation == BeforeAfterSlider.Orientation.HORIZONTAL) sliderOffsetX
                .minus(BeforeAfterSlider.SLIDER_PADDING.dp) else 0.dp,
            y = if (orientation == BeforeAfterSlider.Orientation.VERTICAL) sliderOffsetY
                .minus(BeforeAfterSlider.SLIDER_PADDING.dp) else 0.dp
        )

        SideEffect {
            if (previousMaxWidth != 0f && previousMaxHeight != 0f) {
                offsetX *= maxWidth.div(previousMaxWidth).value
                offsetY *= maxHeight.div(previousMaxHeight).value
            }
            previousMaxWidth = maxWidth.value
            previousMaxHeight = maxHeight.value
        }

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            contentBefore(this@BoxWithConstraints)
        }

        Surface(
            modifier = Modifier.offset(
                x = if (orientation == BeforeAfterSlider.Orientation.HORIZONTAL) sliderOffsetX else 0.dp,
                y = if (orientation == BeforeAfterSlider.Orientation.VERTICAL) sliderOffsetY else 0.dp
            ).fillMaxSize()
        ) {
            Box(
                modifier = Modifier.offset(
                    x = if (orientation == BeforeAfterSlider.Orientation.HORIZONTAL) -sliderOffsetX else 0.dp,
                    y = if (orientation == BeforeAfterSlider.Orientation.VERTICAL) -sliderOffsetY else 0.dp
                ).fillMaxSize().background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                contentAfter(this@BoxWithConstraints)
            }
        }

        Box(
            modifier = sliderModifier
                .composed {
                    when (orientation) {
                        BeforeAfterSlider.Orientation.HORIZONTAL -> requiredWidth(BeforeAfterSlider.SLIDER_THICKNESS.dp).fillMaxHeight()
                        BeforeAfterSlider.Orientation.VERTICAL -> requiredHeight(BeforeAfterSlider.SLIDER_THICKNESS.dp).fillMaxWidth()
                    }
                }
                .then(sliderOffsetModifier)
                .background(Color.White)
                .then(draggingModifier)
        )

        Box(
            modifier = Modifier
                .offset(
                    x = sliderOffsetX.minus(BeforeAfterSlider.SLIDER_CIRCLE_RADIUS.dp),
                    y = sliderOffsetY.minus(BeforeAfterSlider.SLIDER_CIRCLE_RADIUS.dp)
                )
                .size(BeforeAfterSlider.SLIDER_CIRCLE_DIAMETER.dp)
                .clip(CircleShape)
                .background(Color.White)
                .then(draggingModifier)
        )
    }
}