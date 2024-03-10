data class BeforeAfterSliderMeasurements(
    val sliderPadding: Float = SLIDER_PADDING,
    val sliderThickness: Float = SLIDER_THICKNESS,
    val sliderCircleRadius: Float = SLIDER_CIRCLE_RADIUS,
) {
    companion object {
        const val SLIDER_PADDING = 2f
        const val SLIDER_THICKNESS = 4f
        const val SLIDER_CIRCLE_RADIUS = 8f
    }
}