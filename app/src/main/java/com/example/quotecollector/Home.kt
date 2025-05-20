package com.example.quotecollector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.api.RetrofitClient
import com.example.quotecollector.components.AddNewQuoteCard
import com.example.quotecollector.components.Background
import com.example.quotecollector.components.Card
import com.example.quotecollector.components.DeleteConfirmationDialog
import com.example.quotecollector.components.FindQuoteByCategoryDialog
import com.example.quotecollector.components.HeaderSection
import com.example.quotecollector.components.MenuOverlay
import com.example.quotecollector.components.QuoteCard
import com.example.quotecollector.components.QuoteManagementOptions
import com.example.quotecollector.components.QuoteOfTheDayCard
import com.example.quotecollector.components.SearchBar
import com.example.quotecollector.models.Quote
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White
import kotlinx.coroutines.launch

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Poet"
        val userId = intent.getStringExtra("USER_ID") ?: ""
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {
                HomePage(
                    userEmail = userEmail,
                    userId = userId,
                    onLogout = {
                        val intent = Intent(this, LoginPage::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}


@Composable
fun HomePage(
    userEmail: String = "Poet",
    userId: String = "",
    onLogout: () -> Unit = {}
) {
    // State variables
    var searchQuery by rememberSaveable  { mutableStateOf("") }
    var showMenu by rememberSaveable  { mutableStateOf(false) }
    var showAddQuoteCard by rememberSaveable  { mutableStateOf(false) }
    var selectedCategory by rememberSaveable  { mutableStateOf("Motivation") }
    var showDeleteConfirmation by rememberSaveable  { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showFindQuoteByCategoryDialog by rememberSaveable  { mutableStateOf(false) }
    var selectedQuote by remember  { mutableStateOf<Quote?>(null) }
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var filteredQuotes by remember { mutableStateOf<List<Quote>>(emptyList()) }

    // Context and coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val apiService = RetrofitClient.apiService

    // Function to load quotes
    val loadQuotes = {
        scope.launch {
            try {
                val response = apiService.getQuotesByUser(userId)
                if (response.isSuccessful) {
                    quotes = response.body() ?: emptyList()
                    filteredQuotes = quotes
                } else {
                    Toast.makeText(context, "Failed to load quotes: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoadQuotes", "Error loading quotes", e)
            }
        }
    }

    // Load quotes when the composable first enters composition
    LaunchedEffect(userId) {
        loadQuotes()
    }

    // Function to filter quotes by text search
    val filterQuotesByTextSearch = { query: String ->
        if (query.isBlank()) {
            filteredQuotes = quotes
        } else {
            filteredQuotes = quotes.filter { quote ->
                quote.text?.contains(query, ignoreCase = true) == true ||
                        quote.author?.contains(query, ignoreCase = true) == true
            }
        }
    }

    // Sample quote of the day
    val quoteOfTheDay = remember {
        Quote(
            text = "We don't read and write poetry because it's cute. We read and write poetry because we are members of the human race. And the human race is filled with passion. And medicine, law, business, engineering, " +
                    "these are noble pursuits and necessary to sustain life. But poetry, beauty, romance, love, these are what we stay alive for.",
            author = "Dead Poets Society",
            category = "Love",
            userId = "1"
        )
    }

    val deleteAllQuotes = {
        scope.launch {
            isLoading = true
            try {
                val quotesResponse = apiService.getQuotesByUser(userId)
                if (quotesResponse.isSuccessful) {
                    val quotes = quotesResponse.body() ?: emptyList()

                    var deleteCount = 0
                    for (quote in quotes) {
                        val deleteResponse = apiService.deleteQuote(quote.id ?: "")
                        if (deleteResponse.isSuccessful) {
                            deleteCount++
                        }
                    }

                    Toast.makeText(context, "Deleted $deleteCount quotes successfully!", Toast.LENGTH_SHORT).show()
                    loadQuotes() // Reload quotes after deletion
                } else {
                    Toast.makeText(context, "Failed to fetch quotes: ${quotesResponse.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("DeleteQuotes", "Error deleting quotes", e)
            } finally {
                isLoading = false
                showDeleteConfirmation = false
            }
        }
    }

    Background()
    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onDismiss = { showDeleteConfirmation = false },
            onConfirm = { deleteAllQuotes() }
        )
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header section
            item {
                HeaderSection(
                    userEmail = userEmail,
                    onMenuClick = { showMenu = true }
                )
            }

            // Search bar
            item {
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { newQuery ->
                        searchQuery = newQuery
                        filterQuotesByTextSearch(newQuery)
                    },
                    onFilterClick = {
                        showFindQuoteByCategoryDialog = true
                    }
                )
            }

            // Quote of the Day Card
            item {
                QuoteOfTheDayCard(quote = quoteOfTheDay)
            }

            // Add New Quote Card
            if (showAddQuoteCard) {
                item {
                    AddNewQuoteCard(
                        onClose = { showAddQuoteCard = false },
                        onSave = { quote ->
                            loadQuotes()
                            Toast.makeText(
                                context,
                                "Quote saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            showAddQuoteCard = false
                        }
                    )
                }
            }

            // Categories section
            item {
                Text(
                    text = "Categories: Motivation, Love, Life, Philosophy",
                    color = White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Card(
                    title = "Quote Management",
                    content = {
                        // Add New Quote Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Add,
                            text = "Add new quote",
                            onClick = { showAddQuoteCard = true }
                        )

                        // Find Quote by ID Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Search,
                            text = "Find quote by Category",
                            onClick = {
                                showFindQuoteByCategoryDialog = true
                            }
                        )

                        // Delete All Quotes Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Delete,
                            text = "Delete all quotes",
                            onClick = { showDeleteConfirmation = true }
                        )

                        QuoteManagementOptions(
                            icon = Icons.Default.List,
                            text = "View and manage quotes",
                            onClick = {
                                val intent = Intent(context, AllQuotes::class.java)
                                context.startActivity(intent)
                            }
                        )
                    }
                )
            }

            // Search Results
            item {
                Text(
                    text = "Search Results",
                    color = White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }

            // Display search results
            if (searchQuery.isNotBlank()) {
                if (filteredQuotes.isEmpty()) {
                    item {
                        Text(
                            text = "No quotes found matching '$searchQuery'",
                            color = White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                } else {
                    items(filteredQuotes) { quote ->
                        QuoteCard(
                            quote = quote,
                            onEdit = { /* Handle edit */ },
                            onDelete = {
                                scope.launch {
                                    try {
                                        val response = apiService.deleteQuote(quote.id ?: "")
                                        if (response.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Quote deleted successfully!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            loadQuotes() // Reload quotes after deletion
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Failed to delete quote: ${response.code()}",
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
                }
            } else {
                item {
                    Text(
                        text = "Enter text to search quotes by content or author",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    // Menu overlay
    if (showMenu) {
        MenuOverlay(
            onDismiss = { showMenu = false },
            onLogout = {
                onLogout()
                showMenu = false
            }
        )
    }

    // Category filter dialog
    if (showFindQuoteByCategoryDialog) {
        FindQuoteByCategoryDialog(
            onDismiss = { showFindQuoteByCategoryDialog = false },
            onQuoteSelected = { quote ->
                selectedQuote = quote
                Toast.makeText(
                    context,
                    "Selected quote by ${quote.author}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}