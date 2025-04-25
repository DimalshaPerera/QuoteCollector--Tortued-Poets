package com.example.quotecollector.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.ui.theme.cardBackground
@Composable
fun CategoryItem(name: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )

            if (name != "Add new category") {
                Row {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )

                    if (name == "My quote collection") {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .offset(x = (-8).dp)
                                .clip(CircleShape)
                                .background(Color.DarkGray)
                        )
                    }
                }
            }
        }
    }
}