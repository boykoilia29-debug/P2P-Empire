package com.grayscheme.ui.screens.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.grayscheme.data.Estate
import com.grayscheme.data.GameRepository
import com.grayscheme.ui.components.HomeBar
import com.grayscheme.ui.theme.Accent

@Composable
fun AvitoScreen(repo: GameRepository, nav: NavHostController) {
    val state by repo.state.collectAsState()
    var tab by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize().background(Color(0xFF0F0F0F))) {
        Text("Avito", style = MaterialTheme.typography.headlineSmall,
            color = Color.White, modifier = Modifier.padding(16.dp))
        TabRow(selectedTabIndex = tab, containerColor = Color(0xFF1A1A1A), contentColor = Accent) {
            Tab(tab == 0, onClick = { tab = 0 }) { Text("Недвижимость", Modifier.padding(12.dp)) }
            Tab(tab == 1, onClick = { tab = 1 }) { Text("Офисы", Modifier.padding(12.dp)) }
            Tab(tab == 2, onClick = { tab = 2 }) { Text("Техника", Modifier.padding(12.dp)) }
        }

        when (tab) {
            0 -> RealEstateTab(state.realEstate, onBuy = { repo.buyEstate(it) })
            1 -> OfficesTab(state.offices, onBuy = { repo.buyOffice(500_000) },
                onUpgrade = { id, s, p, c -> repo.upgradeOffice(id, s, p, c) })
            else -> TechTab(onBuyPc = { repo.upgradeOffice(state.offices.firstOrNull()?.id ?: "", false, true, false) })
        }

        HomeBar { nav.popBackStack() }
    }
}

@Composable
private fun RealEstateTab(owned: List<Estate>, onBuy: (Estate) -> Unit) {
    val list = remember {
        listOf(
            Estate("1", "Квартира-студия", 1_500_000, 3_000),
            Estate("2", "Двушка в спальнике", 4_000_000, 8_000),
            Estate("3", "Пентхаус", 12_000_000, 25_000)
        )
    }
    Column(Modifier.weight(1f).fillMaxWidth()) {
        Text("Мои: ${owned.size}", color = Color.White, modifier = Modifier.padding(12.dp))
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(12.dp)) {
            items(list, key = { it.id }) { e ->
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(e.title, color = Color.White)
                        Text("₽${e.price} · +${e.incomePerTick}/тик", color = Color.White.copy(alpha = 0.7f))
                    }
                    Button(onClick = { onBuy(e) }, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                        Text("Купить")
                    }
                }
            }
        }
    }
}

@Composable
private fun OfficesTab(
    offices: List<com.grayscheme.data.Office>,
    onBuy: () -> Unit,
    onUpgrade: (String, Boolean, Boolean, Boolean) -> Unit
) {
    Column(Modifier.weight(1f).fillMaxWidth().padding(12.dp)) {
        Button(onClick = onBuy, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
            Text("Купить офис (₽500 000)")
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(offices, key = { it.id }) { o ->
                Column(Modifier.background(Color(0xFF1A1A1A), androidx.compose.foundation.shape.RoundedCornerShape(10.dp)).padding(12.dp)) {
                    Text("Офис", color = Color.White)
                    Text("Staff:${o.staff} PC:${o.pcs} Cards:${o.cards}", color = Color.White.copy(alpha = 0.7f))
                    Text("Расход: -${o.maintenancePerTick}/тик, доход ≈ ${minOf(o.staff, o.pcs, o.cards) * 2000}/тик", color = Color.White.copy(alpha = 0.6f))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(onClick = { onUpgrade(o.id, true, false, false) }) { Text("+Staff") }
                        Button(onClick = { onUpgrade(o.id, false, true, false) }) { Text("+PC") }
                        Button(onClick = { onUpgrade(o.id, false, false, true) }) { Text("+Card") }
                    }
                }
            }
        }
    }
}

@Composable
private fun TechTab(onBuyPc: () -> Unit) {
    Column(Modifier.weight(1f).fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = onBuyPc, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
            Text("Купить ПК для офиса (₽20 000)")
        }
        Text("Флавор-техника: телефоны, ноутбуки — в разработке.", color = Color.White.copy(alpha = 0.7f))
    }
}