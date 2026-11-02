package com.grayscheme

import android.app.Application
import com.grayscheme.data.GameRepository

class GameApp : Application() {
    lateinit var repository: GameRepository
    override fun onCreate() {
        super.onCreate()
        repository = GameRepository(this)
    }
}