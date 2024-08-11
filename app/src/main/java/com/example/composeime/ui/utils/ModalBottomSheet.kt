package com.sanmer.mrepo.ui.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedReceiverParameter")
fun BottomSheetDefaults.expandedShape(size: Dp) =
    RoundedCornerShape(topStart = size, topEnd = size)