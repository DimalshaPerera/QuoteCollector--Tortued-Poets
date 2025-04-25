package com.example.quotecollector.components



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.quotecollector.api.RetrofitClient
import com.example.quotecollector.models.Quote
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindQuoteByCategoryDialog(
    onDismiss: () -> Unit,
    onQuoteSelected: (Quote) -> Unit
) {
    var category by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var foundQuotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var hasSearched by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val apiService = RetrofitClient.apiService

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Find Quotes by Category",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = {
                        category = it
                        errorMessage = ""
                        hasSearched = false
                    },
                    label = { Text("Category") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (hasSearched && foundQuotes.isNotEmpty()) {
                    Text(
                        text = "Found ${foundQuotes.size} quotes",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(foundQuotes) { quote ->
                            QuoteItem(
                                quote = quote,
                                onClick = {
                                    onQuoteSelected(quote)
                                    onDismiss()
                                }
                            )
                        }
                    }
                } else if (hasSearched) {
                    Text(
                        text = "No quotes found for this category",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (category.isBlank()) {
                                errorMessage = "Please enter a category"
                                return@Button
                            }

                            coroutineScope.launch {
                                isLoading = true
                                try {
                                    // Get all quotes and filter by category
                                    val response = apiService.getAllQuotes()
                                    if (response.isSuccessful) {
                                        val allQuotes = response.body() ?: emptyList()
                                        foundQuotes = allQuotes.filter {
                                            it.category?.equals(category, ignoreCase = true) == true
                                        }
                                        hasSearched = true
                                    } else {
                                        errorMessage = "Error: ${response.code()}"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Search")
                        }
                    }
                }
            }
        }
    }
}
