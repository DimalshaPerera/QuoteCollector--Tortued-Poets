package com.example.quotecollector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.quotecollector.components.Background
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.QuoteCollectorTheme
import com.example.quotecollector.ui.theme.White

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: "Poet"
        enableEdgeToEdge()
        setContent {
            QuoteCollectorTheme {

                HomePage(userEmail)
            }
        }
    }
}

@Preview
@Composable
fun HomePage(userEmail: String = "Poet"){
    Background()
    Text(
        text = "Welcome, ${userEmail.substringBefore('@')}!",
        fontFamily = Poppins,
        color = White,
        fontSize = 24.sp,
        textAlign = TextAlign.Center
    )

}
