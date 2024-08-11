package com.example.composeime.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.composeime.R


/**
 * Am 2021-01-12 erstellt.
 */

@Composable
fun BadgeButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
//                .background(keyBackgroundColor)

                .clickable(onClick = onClick)
                .padding(14.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_gear),
            contentDescription = null,
//            tint = keyLabelColor
        )
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
//            .background(keyboardBackgroundColor)
        )
    }

}