package com.grayscheme.ui.screens.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grayscheme.data.GameRepository
import com.grayscheme.ui.components.HomeBar
import com.grayscheme.ui.theme.Accent

@Composable
fun KeeperScreen(repo: GameRepository, nav: NavHostController) {
    val state by repo.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Color(0xFF0B0B0B))) {
        Text("Keeper", style = MaterialTheme.typography.headlineSmall,
            color = Color.White, modifier = Modifier.padding(16.dp))
        state.crypto.forEach { (sym, bal) ->
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
                Text(sym, color = Accent, modifier = Modifier.weight(1f))
                Text("$bal", color = Color.White)
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Accent)
        ) { Text("Перевести") }

        Text("История операций", color = Color.White, modifier = Modifier.padding(16.dp))
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(state.transactions, key = { it.id }) { tx ->
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(tx.title, color = Color.White, modifier = Modifier.weight(1f))
                    Text("${tx.amount}", color = if (tx.amount >= 0) Color(0xFF00C853) else Color(0xFFE53935))
                }
            }
        }
        HomeBar { nav.popBackStack() }
    }

    if (showDialog) {
        var sym by remember { mutableStateOf("USDT") }
        var amount by remember { mutableStateOf("") }
        var addr by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Внутренний перевод") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("USDT", "BTC", "ETH").forEach {
                            FilterChip(sym == it, onClick = { sym = it }, label = { Text(it) })
                        }
                    }
                    OutlinedTextField(amount, { amount = it }, label = { Text("Сумма") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(addr, { addr = it }, label = { Text("Адрес (заглушка)") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    amount.toDoubleOrNull()?.let { repo.internalTransfer(sym, it) }
                    showDialog = false
                }) { Text("Отправить") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Отмена") } }
        )
    }
}