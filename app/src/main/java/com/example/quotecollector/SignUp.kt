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
import com.example.quotecollector.models.User
import com.example.quotecollector.ui.theme.ItaliannoFont
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White
import com.example.quotecollector.utils.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {
                SignUpPage(
                    onNavigateToLogin = {
                        val intent = Intent(this, LoginPage::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onRegistrationSuccess = { email, userId ->
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
fun SignUpPage(
    onNavigateToLogin: () -> Unit = {},
    onRegistrationSuccess: (String, String) -> Unit = { _, _ -> }
) {
    Background()
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

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
                    text = "Enter your email",
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
                    text = "Enter password",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Confirm password",
                    color = White.copy(alpha = 0.6f),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
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
                            contentDescription = "Confirm Password",
                            tint = White.copy(alpha = 0.5f)
                        )
                    }
                )
            }

            // Display error message if any
            if (hasError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = androidx.compose.ui.graphics.Color.Red,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = if (isLoading) "Registering..." else "Register",
                onClick = {
                    // Validate inputs
                    when {
                        email.isBlank() -> {
                            errorMessage = "Email cannot be empty"
                            hasError = true
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "Please enter a valid email"
                            hasError = true
                        }
                        password.isBlank() -> {
                            errorMessage = "Password cannot be empty"
                            hasError = true
                        }
                        password.length < 6 -> {
                            errorMessage = "Password must be at least 6 characters"
                            hasError = true
                        }
                        password != confirmPassword -> {
                            errorMessage = "Passwords do not match"
                            hasError = true
                        }
                        else -> {
                            hasError = false
                            isLoading = true
                            scope.launch {
                                try {
                                    val existingUsersResponse = RetrofitClient.apiService.checkEmailExists(email)

                                    if (existingUsersResponse.isSuccessful &&
                                        (existingUsersResponse.body()?.isNotEmpty() == true)) {

                                        errorMessage = "Email already registered. Please use a different email or sign in."
                                        hasError = true
                                        isLoading = false
                                        return@launch
                                    }
                                    val user = User(
                                        email = email,
                                        password = password,
                                        id = "",
                                        token = ""
                                    )
                                    val response = RetrofitClient.apiService.register(user)

                                    if (response.isSuccessful) {
                                        // Get user data
                                        val userData = response.body()
                                        if (userData != null) {
                                            PreferenceHelper.saveUserData(
                                                context,
                                                email,
                                                userData.id,
                                                userData.id
                                            )

                                            // Show a toast message
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    "Registration successful!",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                // Navigate to home screen with userId
                                                onRegistrationSuccess(email, userData.id)
                                            }
                                        }
                                    } else {
                                        // Handle error response
                                        errorMessage = "Registration failed: ${response.errorBody()?.string() ?: "Unknown error"}"
                                        hasError = true
                                        Log.e("RegisterError", errorMessage)
                                    }
                                } catch (e: Exception) {
                                    // Handle network or other exceptions
                                    errorMessage = "Error: ${e.message ?: "Unknown error"}"
                                    hasError = true
                                    Log.e("RegisterException", "Exception during registration", e)
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
                    text = "Already a member?",
                    color = White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    fontFamily = Poppins
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Sign in",
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Poppins,
                    modifier = Modifier.clickable {
                        onNavigateToLogin()
                    }
                )
            }
        }
    }
}
