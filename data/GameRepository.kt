package com.grayscheme.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class GameRepository(context: Context) {
    private val saveManager = SaveManager(context)
    private val _state = MutableStateFlow(saveManager.load() ?: GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun update(transform: (GameState) -> GameState) {
        _state.value = transform(_state.value)
        saveManager.save(_state.value)
    }

    fun saveNow() = saveManager.save(_state.value)
    fun hasSave(): Boolean = saveManager.exists()

    fun continueGame() {
        saveManager.load()?.let { _state.value = it }
    }

    fun newGame(path: PathType) {
        val st = GameState(
            chosenPath = path,
            telegramChats = listOf(Chat("rocket", "Rocket"))
        )
        _state.value = st
        seedOrders()
        saveManager.save(_state.value)
    }

    // ── P2P ────────────────────────────────────────────────────
    fun seedOrders() {
        update { st ->
            if (st.cryptoOrders.isNotEmpty()) return@update st
            val currencies = listOf("USDT", "BTC", "ETH")
            val orders = (1..8).map { i ->
                val cur = currencies.random()
                val dir = if (i % 2 == 0) "BUY" else "SELL"
                val price = when (cur) {
                    "USDT" -> (90L..100L).random()
                    "BTC" -> (5_000_000L..5_500_000L).random()
                    else -> (250_000L..300_000L).random()
                }
                val amount = if (cur == "USDT") (10..500).random().toDouble() else (0.001 + Math.random() * 0.01)
                Order(UUID.randomUUID().toString(), dir, cur, amount, price, listOf("СБП", "Карта").random())
            }
            st.copy(cryptoOrders = orders)
        }
    }

    fun acceptOrder(orderId: String) {
        update { st ->
            val order = st.cryptoOrders.find { it.id == orderId } ?: return@update st
            val total = (order.amount * order.price).toLong()
            if (order.direction == "BUY") {
                // Сделка с продажей нашей крипты
                if ((st.crypto[order.crypto] ?: 0.0) < order.amount) return@update st
                st.copy(
                    money = st.money + total,
                    crypto = st.crypto + (order.crypto to ((st.crypto[order.crypto] ?: 0.0) - order.amount)),
                    cryptoOrders = st.cryptoOrders - order,
                    transactions = st.transactions + Tx(UUID.randomUUID().toString(), "P2P продажа ${order.crypto}", total)
                )
            } else {
                // Сделка с покупкой крипты
                if (st.money < total) return@update st
                st.copy(
                    money = st.money - total,
                    crypto = st.crypto + (order.crypto to ((st.crypto[order.crypto] ?: 0.0) + order.amount)),
                    cryptoOrders = st.cryptoOrders - order,
                    transactions = st.transactions + Tx(UUID.randomUUID().toString(), "P2P покупка ${order.crypto}", -total)
                )
            }
        }
    }

    fun placeOrder(direction: String, crypto: String, amount: Double, price: Long, method: String) {
        update { st ->
            val order = Order(UUID.randomUUID().toString(), direction, crypto, amount, price, method, ownerId = "me")
            st.copy(cryptoOrders = st.cryptoOrders + order)
        }
    }

    // ── Карты ──────────────────────────────────────────────────
    fun addRocketSaleMessage() {
        val banks = listOf("Сбер", "Тинькофф", "ВТБ", "Альфа", "Райффайзен")
        val bank = banks.random()
        val sum = (50..500).random()
        val price = (2000L..15000L).random()
        update { st ->
            val msg = ChatMessage(
                id = UUID.randomUUID().toString(),
                from = "Rocket",
                text = "Есть карта $bank, сумма $sum тыс, продам за $price ₽",
                kind = "CARD_SALE",
                card = Card(UUID.randomUUID().toString(), bank, sum, price),
                price = price
            )
            val chats = st.telegramChats.toMutableList()
            val idx = chats.indexOfFirst { it.id == "rocket" }
            if (idx >= 0) chats[idx] = chats[idx].copy(messages = chats[idx].messages + msg)
            else chats.add(Chat("rocket", "Rocket", listOf(msg)))
            st.copy(telegramChats = chats)
        }
    }

    fun acceptCardSale(messageId: String) {
        update { st ->
            val msg = st.telegramChats.flatMap { it.messages }.find { it.id == messageId } ?: return@update st
            if (msg.kind != "CARD_SALE" || msg.card == null || st.money < msg.price) return@update st
            st.copy(
                money = st.money - msg.price,
                playerCards = st.playerCards + msg.card,
                telegramChats = st.telegramChats.map { ch ->
                    if (ch.id != "rocket") ch
                    else ch.copy(messages = ch.messages.map { if (it.id == messageId) it.copy(kind = "CARD_BOUGHT") else it })
                },
                transactions = st.transactions + Tx(UUID.randomUUID().toString(), "Покупка карты ${msg.card.bank}", -msg.price)
            )
        }
    }

    fun declineCardSale(messageId: String) {
        update { st ->
            st.copy(
                telegramChats = st.telegramChats.map { ch ->
                    if (ch.id != "rocket") ch
                    else ch.copy(messages = ch.messages.map { if (it.id == messageId) it.copy(kind = "CARD_DECLINED") else it })
                }
            )
        }
    }

    // ── Avito ──────────────────────────────────────────────────
    fun buyEstate(estate: Estate) {
        update { st ->
            if (st.money < estate.price) st
            else st.copy(
                money = st.money - estate.price,
                realEstate = st.realEstate + estate,
                transactions = st.transactions + Tx(UUID.randomUUID().toString(), "Покупка ${estate.title}", -estate.price)
            )
        }
    }

    fun buyOffice(price: Long) {
        update { st ->
            if (st.money < price) st
            else st.copy(
                money = st.money - price,
                offices = st.offices + Office(UUID.randomUUID().toString(), price),
                transactions = st.transactions + Tx(UUID.randomUUID().toString(), "Покупка офиса", -price)
            )
        }
    }

    fun upgradeOffice(id: String, addStaff: Boolean, addPc: Boolean, addCard: Boolean) {
        update { st ->
            val cost = (if (addStaff) 30_000L else 0) + (if (addPc) 20_000L else 0) + (if (addCard) 15_000L else 0)
            if (cost == 0L || st.money < cost) st
            else st.copy(
                money = st.money - cost,
                offices = st.offices.map {
                    if (it.id != id) it
                    else it.copy(
                        staff = it.staff + if (addStaff) 1 else 0,
                        pcs = it.pcs + if (addPc) 1 else 0,
                        cards = it.cards + if (addCard) 1 else 0
                    )
                },
                transactions = st.transactions + Tx(UUID.randomUUID().toString(), "Апгрейд офиса", -cost)
            )
        }
    }

    // ── Keeper ─────────────────────────────────────────────────
    fun internalTransfer(crypto: String, amount: Double) {
        update { st ->
            val bal = st.crypto[crypto] ?: 0.0
            if (bal < amount) st
            else st.copy(
                crypto = st.crypto + (crypto to (bal - amount)),
                transactions = st.transactions + Tx(UUID.randomUUID().toString(), "Перевод $crypto", 0)
            )
        }
    }

    // ── Тик экономики ──────────────────────────────────────────
    fun tick() {
        update { st ->
            var money = st.money
            st.realEstate.forEach { money += it.incomePerTick }
            st.offices.forEach { o ->
                money -= o.maintenancePerTick
                money += minOf(o.staff, o.pcs, o.cards) * 2000L
            }
            st.copy(money = money)
        }
    }
}