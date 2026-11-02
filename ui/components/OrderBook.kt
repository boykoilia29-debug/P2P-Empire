package com.grayscheme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grayscheme.data.Order
import com.grayscheme.ui.theme.Accent
import com.grayscheme.ui.theme.Green
import com.grayscheme.ui.theme.Red

@Composable
fun OrderBook(
    orders: List<Order>,
    onAccept: (String) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(orders, key = { it.id }) { o ->
            // tween-подсветка при изменении
            val alpha = remember { androidx.compose.animation.core.Animatable(0f) }
            LaunchedEffect(o.id) { alpha.animateTo(0.3f); alpha.animateTo(0f, androidx.compose.animation.core.tween(200)) }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.05f).compositeOver(if (o.direction == "BUY") Green.copy(alpha = alpha.value) else Red.copy(alpha = alpha.value)),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        (if (o.direction == "BUY") "КУПИТЬ " else "ПРОДАТЬ ") + o.crypto,
                        color = if (o.direction == "BUY") Green else Red
                    )
                    Text("${o.amount} ${o.crypto} × ${o.price} ₽ · ${o.method}", color = Color.White.copy(alpha = 0.85f))
                }
                Button(
                    onClick = { onAccept(o.id) },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Accent)
                ) { Text("Взять") }
            }
        }
    }
}

private fun Color.compositeOver(bg: Color): Color {
    val a = alpha
    return Color(
        red = red * a + bg.red * (1 - a),
        green = green * a + bg.green * (1 - a),
        blue = blue * a + bg.blue * (1 - a),
        alpha = 1f
    )
}