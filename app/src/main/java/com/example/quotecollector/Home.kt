package com.example.quotecollector

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.api.RetrofitClient
import com.example.quotecollector.components.Background
import com.example.quotecollector.models.Quote
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.ui.theme.cardBackground
import com.example.quotecollector.ui.theme.surfaceVariant
import com.example.quotecollector.utils.PreferenceHelper
import kotlinx.coroutines.launch

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Poet"
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {

                HomePage(userEmail, onLogout = {
                        val intent = Intent(this,LoginPage::class.java)
                        startActivity(intent)
                        finish()
                    
                })
            }
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    userEmail: String = "Poet",
    onLogout: () -> Unit = {}
) {
    // State variables
    var searchQuery by remember { mutableStateOf("") }
    var newQuoteText by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var newQuoteAuthor by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Motivation") }

    // Get context and coroutine scope
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // Sample categories
    val categories = listOf(
        "Motivation" to Icons.Default.Star,
        "Love" to Icons.Default.Favorite,
        "My quote collection" to Icons.Default.List,
        "Add new category" to Icons.Default.Add
    )

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(surfaceVariant)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.roman_figure),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "\"Welcome, ${userEmail.substringBefore('@')}!\"",
                            fontFamily = Poppins,
                            color = White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = White
                        )
                    }
                }
            }

            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
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

            // New quote
            item {
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

                            IconButton(onClick = { isEditMode = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = White
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

                        // Category dropdown (simplified)
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

            // Categories section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categories: Motivation, Love,",
                        color = White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(onClick = { /* Create personal category */ }) {
                        Text(
                            text = "Create personal",
                            color = White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Quote management options
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add new quote",
                                tint = White.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Add new quote",
                                color = White,
                                fontSize = 16.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit author details",
                                tint = White.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Edit author details",
                                color = White,
                                fontSize = 16.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete quote",
                                    tint = White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = "Delete quote",
                                    color = White,
                                    fontSize = 16.sp
                                )
                            }

                            IconButton(onClick = { /* Delete */ }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = White
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit quote content",
                                tint = White.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Edit quote content",
                                color = White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // View all quotes button
            item {
                Button(
                    onClick = { /* View all quotes */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cardBackground
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

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
    if (showMenu) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { showMenu = false }
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
                                showMenu = false
                            }
                    )
                }
            }
        }
    }
}

