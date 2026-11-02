package com.grayscheme.ui.screens.apps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grayscheme.data.GameRepository
import com.grayscheme.ui.components.HomeBar
import com.grayscheme.ui.theme.Accent
import kotlinx.coroutines.launch

@Composable
fun BankScreen(repo: GameRepository, nav: NavHostController) {
    val state by repo.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().background(Color(0xFF0A1730))) {
            Text("Банк", style = MaterialTheme.typography.headlineSmall,
                color = Color.White, modifier = Modifier.padding(16.dp))
            Spacer(Modifier.height(8.dp))
            // Карта
            Canvas(
                Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                val brush = Brush.linearGradient(listOf(Color(0xFF3B4FE0), Color(0xFF1B2A6A)))
                drawRoundRect(brush, size = size, cornerRadius = CornerRadius(20f, 20f))
                // чип
                drawRoundRect(
                    Color(0xFFFFD34E),
                    topLeft = Offset(size.width * 0.08f, size.height * 0.30f),
                    size = Size(size.width * 0.14f, size.height * 0.18f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // полоски-цифры
                drawRoundRect(Color.White.copy(alpha = 0.85f),
                    topLeft = Offset(size.width * 0.08f, size.height * 0.68f),
                    size = Size(size.width * 0.55f, size.height * 0.05f))
                drawRoundRect(Color.White.copy(alpha = 0.6f),
                    topLeft = Offset(size.width * 0.72f, size.height * 0.68f),
                    size = Size(size.width * 0.14f, size.height * 0.05f))
            }
            Spacer(Modifier.height(16.dp))
            Text("Баланс: ₽ ${state.money}", color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        snackbar.showSnackbar(
                            listOf(
                                "Карта перевыпущена успешно",
                                "Перевыпуск одобрен банком",
                                "Новая карта будет доставлена курьером"
                            ).random()
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Accent)
            ) { Text("Перевыпустить карту") }
            Spacer(Modifier.height(12.dp))
            Text("История операций", color = Color.White, modifier = Modifier.padding(horizontal = 24.dp))
            LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)) {
                items(state.transactions, key = { it.id }) { tx ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(tx.title, color = Color.White, modifier = Modifier.weight(1f))
                        Text("${tx.amount}", color = if (tx.amount >= 0) Color(0xFF00C853) else Color(0xFFE53935))
                    }
                }
            }
            HomeBar { nav.popBackStack() }
        }
        SnackbarHost(snackbar, Modifier.align(Alignment.BottomCenter))
    }
}