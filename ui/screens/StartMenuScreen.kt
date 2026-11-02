package com.grayscheme.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grayscheme.data.GameRepository
import com.grayscheme.data.PathType
import com.grayscheme.ui.theme.Accent
import com.grayscheme.ui.theme.DeepBlue
import com.grayscheme.ui.theme.Purple

@Composable
fun StartMenuScreen(repo: GameRepository, nav: NavHostController) {
    val hasSave = remember { repo.hasSave() }
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DeepBlue, Purple)))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("GRAY SCHEME", style = MaterialTheme.typography.headlineLarge, color = Color.White)
        Text("Симулятор P2P-схем", color = Color.White.copy(alpha = 0.7f))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PathCard("P2P с картами", "Классика. Покупка карт.", PathType.CARDS, Modifier.weight(1f)) {
                go(repo, nav, it)
            }
            PathCard("P2P без карт", "Только крипта и СБП.", PathType.NO_CARDS, Modifier.weight(1f)) {
                go(repo, nav, it)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PathCard("Процессинг", "Обработка платежей.", PathType.PROCESSING, Modifier.weight(1f)) {
                go(repo, nav, it)
            }
            PathCard("Скам", "Фейк-карты, высокий риск.", PathType.SCAM, Modifier.weight(1f)) {
                go(repo, nav, it)
            }
        }

        if (hasSave) {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    repo.continueGame()
                    nav.navigate("phone") { popUpTo("start") { inclusive = true } }
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Accent),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Продолжить") }
        }
    }
}

private fun go(repo: GameRepository, nav: NavHostController, path: PathType) {
    repo.newGame(path)
    nav.navigate("phone") { popUpTo("start") { inclusive = true } }
}

@Composable
private fun PathCard(
    title: String,
    subtitle: String,
    path: PathType,
    modifier: Modifier = Modifier,
    onClick: (PathType) -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )
    Column(
        modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .clickable(interactionSource = interaction, indication = null) { onClick(path) }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(title, color = Color.White, style = MaterialTheme.typography.titleMedium)
        Text(subtitle, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
    }
}