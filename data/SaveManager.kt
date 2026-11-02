package com.grayscheme.data

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.File

class SaveManager(context: Context) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    private val file = File(context.filesDir, "save.json")

    fun save(state: GameState) {
        try {
            file.writeText(json.encodeToString(GameState.serializer(), state.copy(savedAt = System.currentTimeMillis())))
        } catch (_: Exception) {}
    }

    fun load(): GameState? = try {
        if (!file.exists()) null
        else json.decodeFromString(GameState.serializer(), file.readText())
    } catch (_: Exception) { null }

    fun exists(): Boolean = file.exists()
}