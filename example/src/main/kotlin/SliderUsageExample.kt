import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import java.nio.file.Files
import kotlin.io.path.Path

fun main(args: Array<String>) {

    require(args.size == 2)

    val (beforeImagePath, afterImagePath) = args

    return singleWindowApplication(
        title = "before-after slider usage",
        state = WindowState(width = 500.dp, height = 500.dp)
    ) {
        val beforeImage = remember {
            loadImageBitmap(
                Files.newInputStream(
                    Path(beforeImagePath)
                )
            )
        }

        val afterImage = remember {
            loadImageBitmap(
                Files.newInputStream(
                    Path(afterImagePath)
                )
            )
        }

        val (percentage, setPercentage) = remember { mutableStateOf<Float?>(null) }

        val (isHorizontal, setIsHorizontal) = remember { mutableStateOf(true) }

        val orientation = remember(isHorizontal) {
            if (isHorizontal) BeforeAfterSliderOrientation.HORIZONTAL else BeforeAfterSliderOrientation.VERTICAL
        }

        val (policy, setPolicy) = remember { mutableStateOf(BeforeAfterSliderDragPolicy.SLIDER) }

        val (minDragPercentage, maxDragPercentage) = remember { .25f to .75f }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BeforeAfterSlider(modifier = Modifier.fillMaxSize(),
                orientation = orientation,
                contentBefore = {
                    Image(beforeImage, null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                },
                contentAfter = {
                    Image(afterImage, null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                },
                dragPolicy = policy,
                minDragPercentage = minDragPercentage,
                maxDragPercentage = maxDragPercentage,
                onPercentageChange = { percentage ->
                    setPercentage(percentage.also(::println))
                })
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Button(onClick = {
                    setIsHorizontal(!isHorizontal)
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
                    Text(orientation.name.lowercase().replaceFirstChar(Char::uppercaseChar))
                }
                Button(onClick = {
                    setPolicy(
                        when (policy) {
                            BeforeAfterSliderDragPolicy.ALL -> BeforeAfterSliderDragPolicy.BEFORE

                            BeforeAfterSliderDragPolicy.BEFORE -> BeforeAfterSliderDragPolicy.AFTER

                            BeforeAfterSliderDragPolicy.AFTER -> BeforeAfterSliderDragPolicy.SLIDER

                            BeforeAfterSliderDragPolicy.SLIDER -> BeforeAfterSliderDragPolicy.ALL
                        }
                    )
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
                    Text(policy.name.lowercase().replaceFirstChar(Char::uppercaseChar))
                }
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (percentage != null) {
                    if (percentage <= minDragPercentage) drawRect(
                        color = Color.Red.copy(alpha = .5f), topLeft = Offset(0f, 0f), size = when (orientation) {
                            BeforeAfterSliderOrientation.HORIZONTAL -> Size(
                                size.width * minDragPercentage, size.height
                            )

                            BeforeAfterSliderOrientation.VERTICAL -> Size(
                                size.width, size.height * minDragPercentage
                            )
                        }
                    )
                    if (percentage >= maxDragPercentage) drawRect(
                        color = Color.Red.copy(alpha = .5f),
                        topLeft = when (orientation) {
                            BeforeAfterSliderOrientation.HORIZONTAL -> Offset(
                                size.width * maxDragPercentage, 0f
                            )

                            BeforeAfterSliderOrientation.VERTICAL -> Offset(
                                0f, size.height * maxDragPercentage
                            )
                        },
                        size = Size(size.width, size.height),
                    )
                }
            }
        }
    }
}