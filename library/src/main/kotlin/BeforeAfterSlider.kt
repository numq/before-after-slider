import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BeforeAfterSlider(
    modifier: Modifier,
    orientation: BeforeAfterSliderOrientation,
    enabled: Boolean = true,
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
) {
    BeforeAfterSliderAccessible(
        modifier = modifier,
        orientation = orientation,
        enabled = enabled,
        contentBefore = contentBefore,
        contentAfter = contentAfter,
        sliderModifier = sliderModifier,
        sliderCircleModifier = sliderCircleModifier,
        measurements = measurements,
        defaultPercentage = defaultPercentage,
        minDragPercentage = minDragPercentage,
        maxDragPercentage = maxDragPercentage,
        dragPolicy = dragPolicy,
        onPercentageChange = onPercentageChange
    )
}