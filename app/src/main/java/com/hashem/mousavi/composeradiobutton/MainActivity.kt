package com.hashem.mousavi.composeradiobutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hashem.mousavi.composeradiobutton.ui.theme.ComposeRadioButtonTheme
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRadioButtonTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    val checked1 = remember {
                        mutableStateOf(false)
                    }

                    val checked2 = remember {
                        mutableStateOf(false)
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RadioButton(checked = checked1) {
                            checked2.value = !it
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        RadioButton(checked = checked2) {
                            checked1.value = !it
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun RadioButton(
    radius: Dp = 50.dp,
    duration: Int = 1000,
    checked: MutableState<Boolean> = mutableStateOf(false),
    onCheckedChanged: (Boolean) -> Unit
) {

    val colorAnim = remember {
        Animatable(Color.Gray)
    }
    val progress = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    LaunchedEffect(checked.value) {
        if (checked.value) {
            launch {
                colorAnim.animateTo(
                    Color(0xFF00BFA5),
                    animationSpec = tween(durationMillis = duration)
                )
            }
            launch {
                progress.animateTo(
                    1f,
                    animationSpec = tween(durationMillis = duration)
                )
            }

        } else {
            launch {
                colorAnim.animateTo(
                    Color.Gray,
                    animationSpec = tween(durationMillis = duration)
                )
            }
            launch {
                progress.animateTo(
                    0f,
                    animationSpec = tween(durationMillis = duration)
                )
            }
        }
    }


    Box(
        modifier = Modifier
            .size(width = radius * 2, height = radius * 2)
    ) {
        Canvas(modifier = Modifier
            .matchParentSize()
            .clickable {
                checked.value = true
                onCheckedChanged(true)
            }) {
            //draw background

            drawCircle(
                color = colorAnim.value,
                radius = radius.toPx()
            )

            translate(left = size.maxDimension / 2f, top = size.maxDimension / 2f) {

                val p = if ((progress.value <= 0.5)) progress.value else 0.5f
                val r = radius.toPx() * 0.8f
                var xs: Float = -(r / sqrt(8.0)).toFloat() - 3f
                var ys = 0f
                var xe: Float = (-(r / sqrt(2.0)) * (0.5f - p)).toFloat() - 3f
                var ye: Float = (r / sqrt(2.0) * p).toFloat()

                drawLine(
                    color = Color.White,
                    start = Offset(xs, ys),
                    end = Offset(xe, ye),
                    strokeWidth = 1.dp.toPx() * (1 + progress.value)
                )

                if (progress.value >= 0.5) {
                    xs = 0f - 3f
                    ys = (r / sqrt(8.0)).toFloat()
                    xe = (r * sqrt(2.0) * (progress.value - 0.5f)).toFloat() - 3f
                    ye = ((3f - 4 * progress.value) * r / sqrt(8.0)).toFloat()

                    drawLine(
                        color = Color.White,
                        start = Offset(xs, ys),
                        end = Offset(xe, ye),
                        strokeWidth = 1.dp.toPx() * (1 + progress.value)
                    )
                }

            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeRadioButtonTheme {
        RadioButton() {

        }
    }
}