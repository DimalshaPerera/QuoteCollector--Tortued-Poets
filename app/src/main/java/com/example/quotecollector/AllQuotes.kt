package com.example.quotecollector

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.api.RetrofitClient
import com.example.quotecollector.components.Background
import com.example.quotecollector.components.QuoteCard
import com.example.quotecollector.models.Quote
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.ui.theme.cardBackground
import com.example.quotecollector.utils.PreferenceHelper
import kotlinx.coroutines.launch

class AllQuotes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {
                AllQuotesScreen(onBackPressed = { finish() })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllQuotesScreen(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Dialog state for editing
    var showEditDialog by remember { mutableStateOf(false) }
    var quoteToEdit by remember { mutableStateOf<Quote?>(null) }
    var editedText by remember { mutableStateOf("") }
    var editedAuthor by remember { mutableStateOf("") }
    var editedCategory by remember { mutableStateOf("") }

    // Get user ID
    val userId = PreferenceHelper.getUserId(context)

    // Load quotes when screen is first displayed
    LaunchedEffect(key1 = true) {
        if (userId != null) {
            scope.launch {
                try {
                    // Get quotes for this user
                    val response = RetrofitClient.apiService.getQuotesByUser(userId)
                    if (response.isSuccessful) {
                        quotes = response.body() ?: emptyList()
                    } else {
                        errorMessage = "Failed to load quotes"
                    }
                } catch (e: Exception) {
                    errorMessage = "Error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        } else {
            errorMessage = "User not logged in"
            isLoading = false
        }
    }

    Background()

    // Edit Dialog
    if (showEditDialog && quoteToEdit != null) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
                quoteToEdit = null
            },
            title = { Text("Edit Quote", color = White) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedText,
                        onValueChange = { editedText = it },
                        label = { Text("Quote Text", color = White.copy(alpha = 0.7f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = White.copy(alpha = 0.7f),
                            unfocusedBorderColor = White.copy(alpha = 0.5f),
                            focusedTextColor = White,
                            unfocusedTextColor = White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editedAuthor,
                        onValueChange = { editedAuthor = it },
                        label = { Text("Author", color = White.copy(alpha = 0.7f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = White.copy(alpha = 0.7f),
                            unfocusedBorderColor = White.copy(alpha = 0.5f),
                            focusedTextColor = White,
                            unfocusedTextColor = White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Simplified category selection
                    Text("Category:", color = White, modifier = Modifier.padding(bottom = 4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val categories = listOf("Motivation", "Love", "Life", "Philosophy")
                        categories.forEach { category ->
                            FilterChip(
                                selected = editedCategory == category,
                                onClick = { editedCategory = category },
                                label = { Text(category) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = White.copy(alpha = 0.3f),
                                    selectedLabelColor = White
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editedText.isNotBlank() && editedAuthor.isNotBlank() && quoteToEdit != null) {
                            // Update the quote
                            val updatedQuote = quoteToEdit!!.copy(
                                text = editedText,
                                author = editedAuthor,
                                category = editedCategory
                            )

                            scope.launch {
                                try {
                                    val response = RetrofitClient.apiService.updateQuote(
                                        updatedQuote.id,
                                        updatedQuote
                                    )

                                    if (response.isSuccessful) {
                                        // Update the local list
                                        quotes = quotes.map {
                                            if (it.id == updatedQuote.id) updatedQuote else it
                                        }

                                        Toast.makeText(
                                            context,
                                            "Quote updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Failed to update quote",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            // Close the dialog
                            showEditDialog = false
                            quoteToEdit = null
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill in all fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cardBackground
                    )
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showEditDialog = false
                        quoteToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF2D2D2D),
            textContentColor = White
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Quotes", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = White
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                quotes.isEmpty() -> {
                    Text(
                        text = "No quotes found. Add some quotes to get started!",
                        color = White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(quotes) { quote ->
                            QuoteCard(
                                quote = quote,
                                onEdit = {
                                    // Handle edit by showing the dialog
                                    quoteToEdit = quote
                                    editedText = quote.text
                                    editedAuthor = quote.author
                                    editedCategory = quote.category
                                    showEditDialog = true
                                },
                                onDelete = {
                                    // Delete quote
                                    scope.launch {
                                        try {
                                            val response = RetrofitClient.apiService.deleteQuote(quote.id)
                                            if (response.isSuccessful) {
                                                // Remove from list
                                                quotes = quotes.filter { it.id != quote.id }
                                                Toast.makeText(
                                                    context,
                                                    "Quote deleted successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Failed to delete quote",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                "Error: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
