package com.grayscheme.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grayscheme.data.ChatMessage
import com.grayscheme.ui.theme.Accent
import com.grayscheme.ui.theme.Red

@Composable
fun ChatMessageRow(
    msg: ChatMessage,
    onAcceptCard: (String) -> Unit,
    onDeclineCard: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Box(
            Modifier
                .background(Color(0xFF1E2649), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(msg.from, color = Accent)
                Spacer(Modifier.height(4.dp))
                Text(msg.text, color = Color.White)
            }
        }
        if (msg.kind == "CARD_SALE") {
            Row(
                Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { onAcceptCard(msg.id) }, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                    Text("Купить за ${msg.price} ₽")
                }
                Button(onClick = { onDeclineCard(msg.id) }, colors = ButtonDefaults.buttonColors(containerColor = Red)) {
                    Text("Отклонить")
                }
            }
        }
    }
}