package com.example.composeime.ui.components.keyboard

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.composeime.keyboard.common.model.Key
import com.example.composeime.keyboard.common.model.Keyboard
import com.example.composeime.ui.theme.keyboardBackgroundColor

/**
 * Am 2021-01-09 erstellt.
 */

@Composable
fun Keyboard(
    keyboard: Keyboard,
    onKeyPress: (Key) -> Unit,
) {

    Log.d("MOVL", "DebugKeyboard:root")

    Box(
        modifier = Modifier
            .background(keyboardBackgroundColor)
            .border(1.dp, Color.Yellow),
        contentAlignment = Alignment.Center
    ) {
        Log.d("MOVL", "DebugKeyboard:box")



        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .graphicsLayer(clip = false)
        ) {
            Log.d("MOVL", "DebugKeyboard:constraintLayout")

            val refs: List<List<Pair<Key, ConstrainedLayoutReference>>> =
                remember {
                    keyboard.map { row ->
                        row.map {
                            it to createRef()
                        }
                    }
                }

            val refsMap: Map<Key, ConstrainedLayoutReference> =
                refs.flatten().toMap()


            refs.forEach { row ->
                createHorizontalChain(*row.map { it.second }.toTypedArray())
            }

            keyboard.forEachIndexed { rowIndex, row ->
                val d = (((1.0f) - row.width) / 2)
                val startGuideline = createGuidelineFromStart(d)
                val endGuideline = createGuidelineFromEnd(d)

                row.forEachIndexed { columnIndex, key ->
                    val ref = refsMap[key] ?: error("Impossible state")

                    val modifier = remember {
                        Modifier
                            .constrainAs(ref) {


                                if (columnIndex == 0) start.linkTo(
                                    startGuideline
                                )
                                if (columnIndex == row.lastIndex) end.linkTo(
                                    endGuideline
                                )

                                //Top
                                if (rowIndex == 0) {
                                    top.linkTo(parent.top)
                                } else {
                                    refsMap[keyboard[rowIndex - 1][0]]?.let {
                                        top.linkTo(it.bottom)
                                    }
                                }

                                height = Dimension.fillToConstraints

                            }
                            .fillMaxWidth(key.weight)
                    }

                    val modifierPressed = remember {
                        Modifier.constrainAs(createRef()) {
                            start.linkTo(ref.start)
                            end.linkTo(ref.end)
                            bottom.linkTo(ref.bottom)
                            width = Dimension.fillToConstraints
                        }
                    }

                    KeyLayout(
                        key = key,
                        modifier = modifier,
                        modifierPressed = modifierPressed,
                        onClick = onKeyPress
                    )
                    SwipePath(modifier = Modifier.fillMaxWidth())
                }

            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwipePath(modifier: Modifier = Modifier) {
    //记录初始按下的点
    var downX by remember {
        mutableFloatStateOf(0f)
    }
    var downY by remember {
        mutableFloatStateOf(0f)
    }
    //绘制路径
    val path = Path()

    Canvas(modifier = modifier
        //使用pointerInteropFilter来获取触摸事件
        .pointerInteropFilter { event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //按下屏幕时，path移动到点击的地方
                    downX = event.x
                    downY = event.y
                    path.moveTo(downX, downY)
                }

                MotionEvent.ACTION_MOVE -> {
                    //手指移动时，绘制线条，并重置downX和downY
                    path.lineTo(downX, downY)
                    downX = event.x
                    downY = event.y
                }
            }
            true
        }, onDraw = {
        //在这里使用一下这个变量，当downX发生变化时，进行recompose
        downX
        //把记录的path绘制出来
        drawPath(
            path,
            color = Color.Red,
            style = Stroke(width = 20f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    })


}



