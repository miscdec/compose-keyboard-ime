package com.example.composeime.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun KeyboardLayout(
    keyboardContent: @Composable () -> Unit,
    barContent: @Composable () -> Unit,
    list: List<String>,
    selectedText: String,
    onSelected: (String) -> Unit,
    onButtonClick: () -> Unit,
) {


//    val fonts = remember { listOf("Category1", "Category2", "Category3", "Category4", "Category5") }
//    val (font, setFont) = remember { mutableStateOf<String>("Default") }
//    Log.d("MOVL", "$font,$fonts")
    Column(
        modifier = Modifier.border(1.dp, Color.Blue)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Red)
        ) {
            val (button, bar) = createRefs()
            createVerticalChain(button, bar)

            KeyboardBar(
                modifier = Modifier.constrainAs(bar) {
                    width = Dimension.fillToConstraints
                    start.linkTo(button.end)
                    end.linkTo(parent.end)
                },
                barContent = barContent,
                list = list,
                onSelected = onSelected,
                selected = selectedText
            )

            BadgeButton(
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(bar.top)
                    bottom.linkTo(bar.bottom)
                    height = Dimension.fillToConstraints
                },
                onClick = onButtonClick
            )

        }
        keyboardContent()
    }

}



