package com.example.quotecollector


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.components.AddNewQuoteCard
import com.example.quotecollector.components.Background
import com.example.quotecollector.components.CategoryItem
import com.example.quotecollector.components.HeaderSection
import com.example.quotecollector.components.MenuOverlay
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
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {
                HomePage(userEmail, onLogout = {
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    finish()
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    userEmail: String = "Poet",
    onLogout: () -> Unit = {}
) {
    // State variables
    var searchQuery by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var showAddQuoteCard by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Motivation") }

    // Context and coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Sample categories
    val categories = listOf(
        "Motivation" to Icons.Default.Star,
        "Love" to Icons.Default.Favorite,
        "My quote collection" to Icons.Default.List,
        "Add new category" to Icons.Default.Add
    )

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

    Background()

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
                    onSearchQueryChange = { searchQuery = it }
                )
            }

            // Quote of the Day Card
            item {
                QuoteOfTheDayCard(quote = quoteOfTheDay)
            }

            // Add New Quote Card (visible only when showAddQuoteCard is true)
            if (showAddQuoteCard) {
                item {
                    AddNewQuoteCard(
                        onClose = { showAddQuoteCard = false },
                        onSave = { quote ->
                            // Handle the saved quote (e.g., add to list, update UI)
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

            // Quote management options
            item {
                com.example.quotecollector.components.Card(
                    title = "Quote Management",
                    content = {
                        // Add New Quote Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Add,
                            text = "Add new quote",
                            onClick = { showAddQuoteCard = true }
                        )

                        // Edit Author Details Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Edit,
                            text = "Edit author details"
                        )

                        // Delete Quote Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Delete,
                            text = "Delete quote"
                        )

                        // Edit Quote Content Option
                        QuoteManagementOptions(
                            icon = Icons.Default.Edit,
                            text = "Edit quote content"
                        )
                    }
                )
            }

            // View all quotes button
            item {
                Button(
                    onClick = {
                        val intent = Intent(context, AllQuotes::class.java)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = com.example.quotecollector.ui.theme.cardBackground
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "View all quotes",
                        color = White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                }


            // Categories list
            items(categories) { (name, icon) ->
                CategoryItem(name = name, icon = icon)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
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
}