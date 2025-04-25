package com.example.quotecollector.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.R
import com.example.quotecollector.ui.theme.ItaliannoFont
import com.example.quotecollector.ui.theme.White

@Composable
fun SplashScreen() {
    Background()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .offset(y = (-20).dp),
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Tortured Poets",
                fontFamily = ItaliannoFont,
                color = White,
                fontSize = 60.sp,
                letterSpacing = 0.5.sp

            )

            Image(
                painter = painterResource(id = R.drawable.roman_figure),
                contentDescription = "Classical image",
                modifier = Modifier.size(280.dp)
            )

            Spacer(modifier = Modifier.height(200.dp))


            CustomButton(
                text = "Get Started",
                onClick ={},
                isGradient = true,
            )
        }
    }
}