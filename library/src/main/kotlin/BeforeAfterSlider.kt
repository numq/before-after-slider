import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BeforeAfterSlider(
    modifier: Modifier,
    orientation: BeforeAfterSliderOrientation,
    contentBefore: @Composable (BoxWithConstraintsScope.() -> Unit)? = null,
    contentAfter: @Composable (BoxWithConstraintsScope.() -> Unit)? = null,
    sliderModifier: Modifier = Modifier.background(Color.White),
    sliderCircleModifier: Modifier = Modifier.background(Color.White),
    measurements: BeforeAfterSliderMeasurements = BeforeAfterSliderMeasurements(),
    defaultPercentage: Float = .5f,
) {

    require(defaultPercentage in (0f..1f))

    BoxWithConstraints(modifier = modifier.padding(measurements.sliderThickness.dp)) {

        var offsetX by remember { mutableStateOf(defaultPercentage * maxWidth.value) }

        var offsetY by remember { mutableStateOf(defaultPercentage * maxHeight.value) }

        val (percentage, setPercentage) = remember {
            mutableStateOf(defaultPercentage)
        }

        val sliderOffsetX by remember(orientation, percentage, maxWidth) {
            derivedStateOf {
                when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> maxWidth.times(percentage).coerceIn(0.dp, maxWidth)

                    BeforeAfterSliderOrientation.VERTICAL -> maxWidth.div(2)
                }
            }
        }

        val sliderOffsetY by remember(orientation, percentage, maxHeight) {
            derivedStateOf {
                when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> maxHeight.div(2)

                    BeforeAfterSliderOrientation.VERTICAL -> maxHeight.times(percentage).coerceIn(0.dp, maxHeight)
                }
            }
        }

        val draggingModifier = Modifier.pointerInput(orientation, maxWidth, maxHeight) {
            detectDragGestures { change, (x, y) ->
                change.consume()
                when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> {
                        offsetX = (offsetX + x).coerceIn(0f, maxWidth.value)

                        setPercentage(offsetX.div(maxWidth.value))
                    }

                    BeforeAfterSliderOrientation.VERTICAL -> {
                        offsetY = (offsetY + y).coerceIn(0f, maxHeight.value)

                        setPercentage(offsetY.div(maxHeight.value))
                    }
                }
            }
        }

        val sliderOffsetModifier = Modifier.offset(
            x = if (orientation == BeforeAfterSliderOrientation.HORIZONTAL) sliderOffsetX.minus(measurements.sliderPadding.dp) else 0.dp,
            y = if (orientation == BeforeAfterSliderOrientation.VERTICAL) sliderOffsetY.minus(measurements.sliderPadding.dp) else 0.dp
        )

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            contentBefore?.invoke(this@BoxWithConstraints) ?: BoxWithConstraints(
                Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center
            ) {
                Text("BEFORE", color = Color.Black, fontSize = minOf(maxWidth, maxHeight).times(.25f).value.sp)
            }
        }

        Surface(
            modifier = Modifier.offset(
                x = if (orientation == BeforeAfterSliderOrientation.HORIZONTAL) sliderOffsetX else 0.dp,
                y = if (orientation == BeforeAfterSliderOrientation.VERTICAL) sliderOffsetY else 0.dp
            ).fillMaxSize()
        ) {
            Box(
                modifier = Modifier.offset(
                    x = if (orientation == BeforeAfterSliderOrientation.HORIZONTAL) -sliderOffsetX else 0.dp,
                    y = if (orientation == BeforeAfterSliderOrientation.VERTICAL) -sliderOffsetY else 0.dp
                ).fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                contentAfter?.invoke(this@BoxWithConstraints) ?: BoxWithConstraints(
                    Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center
                ) {
                    Text("AFTER", color = Color.White, fontSize = minOf(maxWidth, maxHeight).times(.25f).value.sp)
                }
            }
        }

        Box(
            modifier = Modifier.composed {
                when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> requiredWidth(measurements.sliderThickness.dp).fillMaxHeight()

                    BeforeAfterSliderOrientation.VERTICAL -> requiredHeight(measurements.sliderThickness.dp).fillMaxWidth()
                }
            }.then(sliderOffsetModifier).then(draggingModifier).then(sliderModifier)
        )

        if (measurements.sliderCircleRadius > 0f) Box(
            modifier = Modifier.offset(
                x = sliderOffsetX.minus(measurements.sliderCircleRadius.dp),
                y = sliderOffsetY.minus(measurements.sliderCircleRadius.dp)
            ).size(measurements.sliderCircleRadius.dp.times(2f)).clip(CircleShape).then(draggingModifier)
                .then(sliderCircleModifier)
        )
    }
}