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
    defaultPercentage: Float = BeforeAfterSliderDefaults.DefaultPercentage,
    minDragPercentage: Float = BeforeAfterSliderDefaults.MinDragPercentage,
    maxDragPercentage: Float = BeforeAfterSliderDefaults.MaxDragPercentage,
    dragPolicy: BeforeAfterSliderDragPolicy = BeforeAfterSliderDragPolicy.SLIDER,
    onPercentageChange: (Float) -> Unit = {},
    overriddenPercentage: Float? = null,
) {
    if (overriddenPercentage != null) require(overriddenPercentage in (minDragPercentage..maxDragPercentage))

    require(defaultPercentage in (minDragPercentage..maxDragPercentage))

    val enabled = remember(overriddenPercentage) { overriddenPercentage == null }

    val (percentage, setPercentage) = remember(defaultPercentage) {
        mutableStateOf(defaultPercentage)
    }

    LaunchedEffect(overriddenPercentage, percentage) {
        onPercentageChange(overriddenPercentage ?: percentage)
    }

    BoxWithConstraints(modifier = modifier.padding(measurements.sliderThickness.dp)) {

        val rootScope = remember(this) { this }

        val (maxWidth, maxHeight) = maxWidth to maxHeight

        var offsetX by remember { mutableStateOf(defaultPercentage * maxWidth.value) }

        var offsetY by remember { mutableStateOf(defaultPercentage * maxHeight.value) }

        val draggingModifier = Modifier.pointerInput(
            enabled, orientation, maxWidth, maxHeight, minDragPercentage, maxDragPercentage
        ) {
            detectDragGestures { change, (x, y) ->
                change.consume()
                if (enabled) when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> {
                        offsetX = (offsetX + x).coerceIn(0f, maxWidth.value)

                        setPercentage(
                            offsetX.div(maxWidth.value).coerceIn(minDragPercentage, maxDragPercentage)
                        )
                    }

                    BeforeAfterSliderOrientation.VERTICAL -> {
                        offsetY = (offsetY + y).coerceIn(0f, maxHeight.value)

                        setPercentage(
                            offsetY.div(maxHeight.value).coerceIn(minDragPercentage, maxDragPercentage)
                        )
                    }
                }
            }
        }

        val sliderOffsetX by remember(orientation, overriddenPercentage, percentage, maxWidth) {
            derivedStateOf {
                when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> maxWidth.times(overriddenPercentage ?: percentage)
                        .coerceIn(0.dp, maxWidth)

                    BeforeAfterSliderOrientation.VERTICAL -> maxWidth.div(2)
                }
            }
        }

        val sliderOffsetY by remember(orientation, overriddenPercentage, percentage, maxHeight) {
            derivedStateOf {
                when (orientation) {
                    BeforeAfterSliderOrientation.HORIZONTAL -> maxHeight.div(2)

                    BeforeAfterSliderOrientation.VERTICAL -> maxHeight.times(overriddenPercentage ?: percentage)
                        .coerceIn(0.dp, maxHeight)
                }
            }
        }

        val sliderOffsetModifier = Modifier.offset(
            x = if (orientation == BeforeAfterSliderOrientation.HORIZONTAL) sliderOffsetX.minus(measurements.sliderPadding.dp) else 0.dp,
            y = if (orientation == BeforeAfterSliderOrientation.VERTICAL) sliderOffsetY.minus(measurements.sliderPadding.dp) else 0.dp
        )

        SideEffect {
            offsetX = percentage * maxWidth.value
            offsetY = percentage * maxHeight.value
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().then(
                if (dragPolicy == BeforeAfterSliderDragPolicy.ALL) draggingModifier else Modifier
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .then(if (dragPolicy == BeforeAfterSliderDragPolicy.BEFORE) draggingModifier else Modifier),
                contentAlignment = Alignment.Center
            ) {
                contentBefore?.invoke(rootScope) ?: BoxWithConstraints(
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
                    ).fillMaxSize()
                        .then(if (dragPolicy == BeforeAfterSliderDragPolicy.AFTER) draggingModifier else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    contentAfter?.invoke(rootScope) ?: BoxWithConstraints(
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
                }.then(sliderOffsetModifier)
                    .then(if (dragPolicy == BeforeAfterSliderDragPolicy.SLIDER) draggingModifier else Modifier)
                    .then(sliderModifier)
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
}