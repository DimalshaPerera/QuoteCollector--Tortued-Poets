package com.example.quotecollector.components
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.api.RetrofitClient
import com.example.quotecollector.models.Quote

import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.ui.theme.cardBackground
import com.example.quotecollector.utils.PreferenceHelper
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewQuoteCard(
    onClose: () -> Unit,
    onSave: (Quote) -> Unit
) {
    // State variables
    var newQuoteText by remember { mutableStateOf("") }
    var newQuoteAuthor by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Philosophy") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Get context for Toast messages
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Add new quote",
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                IconButton(onClick = onClose) {
                    // Simple X icon for closing
                    Text(
                        text = "×",
                        color = White,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quote text field
            OutlinedTextField(
                value = newQuoteText,
                onValueChange = { newQuoteText = it },
                placeholder = {
                    Text(
                        "Type your quote here...",
                        color = White.copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White.copy(alpha = 0.3f),
                    unfocusedBorderColor = White.copy(alpha = 0.1f),
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Author field
            OutlinedTextField(
                value = newQuoteAuthor,
                onValueChange = { newQuoteAuthor = it },
                placeholder = {
                    Text(
                        "Author name",
                        color = White.copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White.copy(alpha = 0.3f),
                    unfocusedBorderColor = White.copy(alpha = 0.1f),
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category selection
            Text("Category: $selectedCategory", color = White.copy(alpha = 0.7f))
            Row(modifier = Modifier.fillMaxWidth()) {
                val categories = listOf("Motivation", "Love", "Life", "Philosophy")
                categories.forEach { category ->
                    TextButton(
                        onClick = { selectedCategory = category },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (selectedCategory == category) White else White.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(category)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Success message
            if (showSuccessMessage) {
                Text(
                    text = "Quote added successfully!",
                    color = Color.Green,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    if (newQuoteText.isNotBlank() && newQuoteAuthor.isNotBlank()) {
                        isLoading = true

                        // Get userId from preferences
                        val userId = PreferenceHelper.getUserId(context) ?: ""

                        // Create quote object
                        val quote = Quote(
                            text = newQuoteText,
                            author = newQuoteAuthor,
                            category = selectedCategory,
                            userId = userId
                        )

                        // Save to API
                        scope.launch {
                            try {
                                val response = RetrofitClient.apiService.createQuote(quote)

                                if (response.isSuccessful) {
                                    // Notify parent component
                                    onSave(quote)

                                    // Clear form and show success message
                                    newQuoteText = ""
                                    newQuoteAuthor = ""
                                    showSuccessMessage = true

                                    // Hide success message after 3 seconds
                                    scope.launch {
                                        kotlinx.coroutines.delay(3000)
                                        showSuccessMessage = false
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Failed to add quote",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter quote text and author",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                enabled = !isLoading,
                modifier = Modifier.width(100.dp)
            ) {
                Text(if (isLoading) "Saving..." else "Save", color = White)
            }
        }
    }
}
