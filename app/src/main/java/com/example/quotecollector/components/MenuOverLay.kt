package com.example.quotecollector.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.ui.theme.cardBackground

@Composable
fun MenuOverlay(
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .padding(start = 16.dp, top = 70.dp)
                .width(150.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Logout",
                    color = White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            onLogout()
                        }
                )
            }
        }
    }
}