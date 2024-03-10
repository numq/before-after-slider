import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.singleWindowApplication
import java.nio.file.Files
import kotlin.io.path.Path

fun main(args: Array<String>) {

    require(args.size == 2)

    val (beforeImagePath, afterImagePath) = args

    return singleWindowApplication(title = "Before after slider") {
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

        val (isHorizontal, setIsHorizontal) = remember {
            mutableStateOf(true)
        }

        val orientation = remember(isHorizontal) {
            if (isHorizontal) BeforeAfterSlider.Orientation.HORIZONTAL else BeforeAfterSlider.Orientation.VERTICAL
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            BeforeAfter(
                modifier = Modifier.fillMaxSize(),
                orientation = orientation,
                contentBefore = {
                    Image(beforeImage, null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                },
                contentAfter = {
                    Image(afterImage, null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                }
            )
            Button(onClick = {
                setIsHorizontal(!isHorizontal)
            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black)) {
                Text(orientation.name.lowercase().replaceFirstChar(Char::uppercaseChar))
            }
        }
    }
}