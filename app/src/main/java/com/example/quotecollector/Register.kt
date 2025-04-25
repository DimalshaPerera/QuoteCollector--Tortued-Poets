package com.example.quotecollector

import android.os.Bundle
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.components.Background
import com.example.quotecollector.components.CustomButton
import com.example.quotecollector.ui.theme.ItaliannoFont
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {
               RegisterPage()
            }
        }
    }
}

@Preview
@Composable
fun RegisterPage() {
    Background()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and title
            Spacer(modifier = Modifier.height(48.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.roman_figure),
                    contentDescription = "Classical image",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tortured Poets",
                    fontFamily = ItaliannoFont,
                    color = White,
                    fontSize = 60.sp,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Join the  The Tortured poets department community now!",
                    color = White.copy(alpha = 0.6f),
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

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
                    value = "",
                    onValueChange = { },
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
                    value = "",
                    onValueChange = { },
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

            Spacer(modifier = Modifier.height(40.dp))

            CustomButton(
                text = "Register",
                onClick = { /* Handle registration */ },
                width = Int.MAX_VALUE,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(100.dp))


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
                    modifier = Modifier.clickable { }
                )
            }
        }
    }

}
