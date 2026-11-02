package com.grayscheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.grayscheme.ui.AppRoot
import com.grayscheme.ui.theme.P2PTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = (application as GameApp).repository
        setContent { P2PTheme { AppRoot(repo) } }
    }
    override fun onStop() {
        super.onStop()
        (application as GameApp).repository.saveNow()
    }
}