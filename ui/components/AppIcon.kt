package com.grayscheme.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun TelegramIcon(modifier: Modifier = Modifier) = Canvas(modifier.size(56.dp)) {
    drawCircle(Color(0xFF29A6E0))
    val p = Path().apply {
        moveTo(size.width * 0.20f, size.height * 0.55f)
        lineTo(size.width * 0.85f, size.height * 0.28f)
        lineTo(size.width * 0.65f, size.height * 0.80f)
        lineTo(size.width * 0.52f, size.height * 0.62f)
        lineTo(size.width * 0.40f, size.height * 0.72f)
        lineTo(size.width * 0.42f, size.height * 0.55f)
        close()
    }
    drawPath(p, Color.White)
}

@Composable
fun BibitIcon(modifier: Modifier = Modifier) = Canvas(modifier.size(56.dp)) {
    val brush = Brush.linearGradient(listOf(Color(0xFFFFB347), Color(0xFFF7931A)))
    drawRoundRect(brush, size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f))
    val path = Path().apply {
        // «B»
        moveTo(size.width * 0.35f, size.height * 0.25f)
        lineTo(size.width * 0.55f, size.height * 0.25f)
        quadraticBezierTo(size.width * 0.72f, size.height * 0.25f, size.width * 0.72f, size.height * 0.42f)
        quadraticBezierTo(size.width * 0.72f, size.height * 0.50f, size.width * 0.58f, size.height * 0.50f)
        lineTo(size.width * 0.55f, size.height * 0.50f)
        moveTo(size.width * 0.35f, size.height * 0.50f)
        lineTo(size.width * 0.60f, size.height * 0.50f)
        quadraticBezierTo(size.width * 0.78f, size.height * 0.50f, size.width * 0.78f, size.height * 0.68f)
        quadraticBezierTo(size.width * 0.78f, size.height * 0.75f, size.width * 0.55f, size.height * 0.75f)
        lineTo(size.width * 0.35f, size.height * 0.75f)
    }
    drawPath(path, Color.White, style = Stroke(width = size.width * 0.10f))
}

@Composable
fun KeeperIcon(modifier: Modifier = Modifier) = Canvas(modifier.size(56.dp)) {
    drawRoundRect(
        color = Color(0xFF1B1F2A),
        size = size,
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
    )
    val p = Path().apply {
        moveTo(size.width * 0.20f, size.height * 0.35f)
        lineTo(size.width * 0.80f, size.height * 0.35f)
        lineTo(size.width * 0.80f, size.height * 0.70f)
        lineTo(size.width * 0.20f, size.height * 0.70f)
        close()
    }
    drawPath(p, Color(0xFF6C5CE7))
    drawCircle(Color.White, radius = size.width * 0.05f, center = Offset(size.width * 0.68f, size.height * 0.52f))
}

@Composable
fun AvitoIcon(modifier: Modifier = Modifier) = Canvas(modifier.size(56.dp)) {
    drawRoundRect(color = Color(0xFFF2F2F2), size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f))
    val colors = listOf(Color(0xFF00AAFF), Color(0xFFFF4B4B), Color(0xFF00C853), Color(0xFFFFB800))
    val r = size.width * 0.12f
    drawCircle(colors[0], r, Offset(size.width * 0.30f, size.height * 0.30f))
    drawCircle(colors[1], r, Offset(size.width * 0.70f, size.height * 0.30f))
    drawCircle(colors[2], r, Offset(size.width * 0.30f, size.height * 0.70f))
    drawCircle(colors[3], r, Offset(size.width * 0.70f, size.height * 0.70f))
}

@Composable
fun BankIcon(modifier: Modifier = Modifier) = Canvas(modifier.size(56.dp)) {
    val brush = Brush.linearGradient(listOf(Color(0xFF1E5FFF), Color(0xFF0040B0)))
    drawRoundRect(brush, size = size, cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f))
    drawRoundRect(Color.White, topLeft = Offset(size.width * 0.18f, size.height * 0.40f),
        size = Size(size.width * 0.64f, size.height * 0.08f))
    drawRoundRect(Color.White.copy(alpha = 0.7f), topLeft = Offset(size.width * 0.18f, size.height * 0.60f),
        size = Size(size.width * 0.40f, size.height * 0.06f))
}