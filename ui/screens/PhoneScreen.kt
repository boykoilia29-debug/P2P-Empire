package com.grayscheme.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grayscheme.data.GameRepository
import com.grayscheme.data.PathType
import com.grayscheme.ui.components.*
import com.grayscheme.ui.theme.DeepBlue
import com.grayscheme.ui.theme.Purple
import kotlinx.coroutines.delay

@Composable
fun PhoneScreen(repo: GameRepository, nav: NavHostController) {
    val state by repo.state.collectAsState()

    // Тик экономики каждые 10 секунд
    LaunchedEffect(Unit) {
        while (true) {
            delay(10_000)
            repo.tick()
        }
    }

    // Генерация событий о продаже карт (только для CARDS/PROCESSING/SCAM)
    LaunchedEffect(Unit) {
        val path = state.chosenPath ?: return@LaunchedEffect
        if (path != PathType.CARDS && path != PathType.PROCESSING && path != PathType.SCAM) return@LaunchedEffect
        if (state.cardSaleEventsLeft == 0) {
            repo.update { it.copy(cardSaleEventsLeft = (1..3).random()) }
        }
        while (true) {
            val current = repo.state.value
            if (current.cardSaleEventsLeft <= 0) break
            delay((30_000L..90_000L).random())
            if (repo.state.value.cardSaleEventsLeft <= 0) break
            repo.update { it.copy(cardSaleEventsLeft = it.cardSaleEventsLeft - 1) }
            repo.addRocketSaleMessage()
        }
    }

    Box(Modifier.fillMaxSize()) {
        // Фон
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(DeepBlue, Purple)))
        )
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Мой телефон", color = Color.White)
            Text("₽ ${state.money}", color = Color.White)

            val icons = listOf(
                Triple("Telegram", "telegram") { TelegramIcon(Modifier.size(56.dp)) },
                Triple("Bibit", "bibit") { BibitIcon(Modifier.size(56.dp)) },
                Triple("Keeper", "keeper") { KeeperIcon(Modifier.size(56.dp)) },
                Triple("Avito", "avito") { AvitoIcon(Modifier.size(56.dp)) },
                Triple("Bank", "bank") { BankIcon(Modifier.size(56.dp)) }
            )
            for (row in icons.chunked(4)) {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    row.forEach { (name, route, icon) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { nav.navigate(route) }
                                .padding(6.dp)
                        ) {
                            icon()
                            Spacer(Modifier.height(4.dp))
                            Text(name, color = Color.White, maxLines = 1)
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            HomeBarPlaceholder()
        }
    }
}

@Composable
private fun HomeBarPlaceholder() {
    // на главном экране HomeBar не нужен, оставляем пустое место под системный бар
    Spacer(Modifier.height(20.dp))
}