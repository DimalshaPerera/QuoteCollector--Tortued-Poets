package com.example.quotecollector.components
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.quotecollector.ui.theme.White
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = {
            Text(
                "Search by quote or author",
                color = White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = White.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            Row {
                Text(
                    text = "Filter by",
                    color = White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Filter",
                    tint = White.copy(alpha = 0.7f)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = White.copy(alpha = 0.3f),
            unfocusedBorderColor = White.copy(alpha = 0.1f),
            focusedTextColor = White,
            unfocusedTextColor = White,
            cursorColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    )
}
