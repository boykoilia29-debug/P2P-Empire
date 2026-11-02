package com.grayscheme.ui.screens.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grayscheme.data.GameRepository
import com.grayscheme.data.Order
import com.grayscheme.ui.components.ChatMessageRow
import com.grayscheme.ui.components.HomeBar
import com.grayscheme.ui.components.OrderBook
import com.grayscheme.ui.theme.Accent
import com.grayscheme.ui.theme.Green
import com.grayscheme.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelegramScreen(repo: GameRepository, nav: NavHostController) {
    val state by repo.state.collectAsState()
    var openChat by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().background(Color(0xFF17212B))
    ) {
        if (openChat == null) {
            Text("Telegram", color = Color.White, style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp))
            LazyColumn(Modifier.weight(1f)) {
                items(state.telegramChats, key = { it.id }) { ch ->
                    Row(
                        Modifier.fillMaxWidth()
                            .clickable { openChat = ch.id }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(40.dp).background(Accent, RoundedCornerShape(50))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(ch.title, color = Color.White)
                            Text(
                                ch.messages.lastOrNull()?.text ?: "Нет сообщений",
                                color = Color.White.copy(alpha = 0.6f),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        } else {
            RocketChat(repo, openChat!!, onBack = { openChat = null })
        }
        HomeBar { nav.popBackStack() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RocketChat(repo: GameRepository, chatId: String, onBack: () -> Unit) {
    val state by repo.state.collectAsState()
    val chat = state.telegramChats.find { it.id == chatId } ?: return
    var tab by remember { mutableIntStateOf(0) }
    var showSheet by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Color(0xFF0E1621))) {
        Row(
            Modifier.fillMaxWidth().background(Color(0xFF17212B)).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("←", color = Color.White) }
            Text(chat.title, color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        TabRow(selectedTabIndex = tab, containerColor = Color(0xFF17212B), contentColor = Accent) {
            Tab(tab == 0, onClick = { tab = 0 }) { Text("Чат", Modifier.padding(12.dp)) }
            Tab(tab == 1, onClick = { tab = 1 }) { Text("Купить", Modifier.padding(12.dp)) }
            Tab(tab == 2, onClick = { tab = 2 }) { Text("Продать", Modifier.padding(12.dp)) }
        }

        when (tab) {
            0 -> LazyColumn(
                Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(chat.messages, key = { it.id }) { m ->
                    ChatMessageRow(
                        msg = m,
                        onAcceptCard = { repo.acceptCardSale(it) },
                        onDeclineCard = { repo.declineCardSale(it) }
                    )
                }
            }
            1 -> OrderBook(
                orders = state.cryptoOrders.filter { it.direction == "SELL" },
                onAccept = { repo.acceptOrder(it) }
            )
            else -> Column(Modifier.weight(1f).padding(12.dp)) {
                Text("Мои ордера", color = Color.White)
                OrderBook(
                    orders = state.cryptoOrders.filter { it.ownerId == "me" },
                    onAccept = { /* свои не принимаем */ }
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { showSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Accent),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Создать ордер") }
            }
        }

        if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }, containerColor = Color(0xFF17212B)) {
                OrderForm(onSubmit = { dir, cur, amt, price, method ->
                    repo.placeOrder(dir, cur, amt, price, method)
                    showSheet = false
                })
            }
        }
    }
}

@Composable
private fun OrderForm(onSubmit: (String, String, Double, Long, String) -> Unit) {
    var dir by remember { mutableStateOf("SELL") }
    var cur by remember { mutableStateOf("USDT") }
    var amount by remember { mutableStateOf("100") }
    var price by remember { mutableStateOf("95") }
    var method by remember { mutableStateOf("СБП") }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Направление", color = Color.White)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(dir == "BUY", onClick = { dir = "BUY" }, label = { Text("Купить") })
            FilterChip(dir == "SELL", onClick = { dir = "SELL" }, label = { Text("Продать") })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("USDT", "BTC", "ETH").forEach {
                FilterChip(cur == it, onClick = { cur = it }, label = { Text(it) })
            }
        }
        OutlinedTextField(amount, { amount = it }, label = { Text("Сумма") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next))
        OutlinedTextField(price, { price = it }, label = { Text("Цена ₽") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(method == "СБП", onClick = { method = "СБП" }, label = { Text("СБП") })
            FilterChip(method == "Карта", onClick = { method = "Карта" }, label = { Text("Карта") })
        }
        Button(
            onClick = {
                val a = amount.toDoubleOrNull() ?: return@Button
                val p = price.toLongOrNull() ?: return@Button
                onSubmit(dir, cur, a, p, method)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Accent)
        ) { Text("Разместить") }
    }
}