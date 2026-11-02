package com.grayscheme.data

import kotlinx.serialization.Serializable

enum class PathType { CARDS, NO_CARDS, PROCESSING, SCAM }

@Serializable data class Card(val id: String, val bank: String, val sumThousands: Int, val boughtPrice: Long)

@Serializable data class Order(
    val id: String,
    val direction: String,     // "BUY" — хотим купить крипту, "SELL" — продать
    val crypto: String,
    val amount: Double,
    val price: Long,           // цена в рублях за 1 единицу
    val method: String,
    val ownerId: String = "system"
)

@Serializable data class ChatMessage(
    val id: String,
    val from: String,
    val text: String,
    val kind: String = "TEXT",  // TEXT | CARD_SALE | CARD_BOUGHT | CARD_DECLINED
    val card: Card? = null,
    val price: Long = 0L,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable data class Chat(val id: String, val title: String, val messages: List<ChatMessage> = emptyList())

@Serializable data class Estate(val id: String, val title: String, val price: Long, val incomePerTick: Long)

@Serializable data class Office(
    val id: String,
    val price: Long,
    val staff: Int = 0,
    val pcs: Int = 0,
    val cards: Int = 0,
    val maintenancePerTick: Long = 500L
)

@Serializable data class Tx(val id: String, val title: String, val amount: Long, val timestamp: Long = System.currentTimeMillis())

@Serializable data class GameState(
    val chosenPath: PathType? = null,
    val money: Long = 50_000,
    val crypto: Map<String, Double> = mapOf("USDT" to 0.0, "BTC" to 0.0, "ETH" to 0.0),
    val playerCards: List<Card> = emptyList(),
    val cryptoOrders: List<Order> = emptyList(),
    val telegramChats: List<Chat> = emptyList(),
    val cardSaleEventsLeft: Int = 0,
    val realEstate: List<Estate> = emptyList(),
    val offices: List<Office> = emptyList(),
    val transactions: List<Tx> = emptyList(),
    val savedAt: Long = 0
)