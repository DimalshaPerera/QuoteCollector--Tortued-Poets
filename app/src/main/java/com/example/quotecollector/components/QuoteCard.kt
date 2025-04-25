package com.example.quotecollector.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.models.Quote
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.ui.theme.cardBackground

@Composable
fun QuoteCard(
    quote: Quote,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Category
            Text(
                text = quote.category,
                color = White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Quote text
            Text(
                text = "\"${quote.text}\"",
                color = White,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Author
            Text(
                text = "— ${quote.author}",
                color = White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = White.copy(alpha = 0.7f)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}