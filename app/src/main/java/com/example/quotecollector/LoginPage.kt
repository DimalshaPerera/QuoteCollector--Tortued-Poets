package com.example.quotecollector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.api.RetrofitClient
import com.example.quotecollector.components.Background
import com.example.quotecollector.components.CustomButton
import com.example.quotecollector.ui.theme.ItaliannoFont
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.utils.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {
                Login(
                    onNavigateToSignUp = {
                        val intent = Intent(this, SignUp::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onLoginSuccess = { email, userId ->
                        val intent = Intent(this, Home::class.java)
                        intent.putExtra("USER_EMAIL", email)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun Login(
    onNavigateToSignUp: () -> Unit = {},
    onLoginSuccess:(String, String) -> Unit = { _, _ -> }
) {
    Background()
    var email by rememberSaveable  { mutableStateOf("") }
    var password by rememberSaveable  { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Get context for API calls and SharedPreferences
    val context = LocalContext.current
    // Get CoroutineScope for launching API calls
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.roman_figure),
                contentDescription = "Classical image",
                modifier = Modifier.size(100.dp)
            )
            // Logo and title
            Text(
                text = "Tortured Poets",
                fontFamily = ItaliannoFont,
                color = White,
                fontSize = 60.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Email",
                    color = White.copy(alpha = 0.6f),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email@email.com", color = White.copy(alpha = 0.3f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = White.copy(alpha = 0.5f),
                        unfocusedBorderColor = White.copy(alpha = 0.2f),
                        focusedTextColor = White,
                        unfocusedTextColor = White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = White.copy(alpha = 0.5f)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Password",
                    color = White.copy(alpha = 0.6f),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••••••", color = White.copy(alpha = 0.3f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = White.copy(alpha = 0.5f),
                        unfocusedBorderColor = White.copy(alpha = 0.2f),
                        focusedTextColor = White,
                        unfocusedTextColor = White
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = White.copy(alpha = 0.5f)
                        )
                    }
                )
            }


            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = androidx.compose.ui.graphics.Color.Red,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = if (isLoading) "Logging in..." else "Sign In",
                onClick = {
                    // Validate inputs
                    when {
                        email.isBlank() -> errorMessage = "Email cannot be empty"
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                            errorMessage = "Please enter a valid email"
                        password.isBlank() -> errorMessage = "Password cannot be empty"
                        else -> {
                            errorMessage = null
                            isLoading = true

                            // Call the login API with MockAPI
                            scope.launch {
                                try {
                                    // Get all users with matching email
                                    val response = RetrofitClient.apiService.login(email)

                                    if (response.isSuccessful) {
                                        val users = response.body()
                                        // Find user with matching password
                                        val user = users?.find { it.email == email && it.password == password }

                                        if (user != null) {
                                            // Save user data to SharedPreferences
                                            PreferenceHelper.saveUserData(
                                                context,
                                                user.email,
                                                user.id,
                                                user.id
                                            )

                                            // Show a toast message
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    "Login successful!",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                // Navigate to home screen
                                                onLoginSuccess(user.email,user.id)
                                            }
                                        } else {
                                            errorMessage = "Invalid email or password"
                                        }
                                    } else {
                                        // Handle error response
                                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                                        errorMessage = "Login failed: $errorBody"
                                        Log.e("LoginError", errorMessage!!)
                                    }
                                } catch (e: Exception) {
                                    // Handle network or other exceptions
                                    errorMessage = "Error: ${e.message ?: "Unknown error"}"
                                    Log.e("LoginException", "Exception during login", e)
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                },
                enabled = !isLoading,
                width = Int.MAX_VALUE,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Not a member yet?",
                    color = White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontFamily = Poppins
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Sign up",
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Poppins,
                    modifier = Modifier.clickable {
                        onNavigateToSignUp()
                    }
                )
            }
        }
    }
}