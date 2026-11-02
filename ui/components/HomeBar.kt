package com.grayscheme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeBar(onHome: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onHome() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .width(120.dp)
                .height(5.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(50))
        )
    }
}