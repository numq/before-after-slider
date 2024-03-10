interface BeforeAfterSlider {
    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    companion object {
        internal const val SLIDER_PADDING = 2
        internal const val SLIDER_THICKNESS = 4
        internal const val SLIDER_CIRCLE_RADIUS = 8
        internal const val SLIDER_CIRCLE_DIAMETER = SLIDER_CIRCLE_RADIUS * 2
    }
}