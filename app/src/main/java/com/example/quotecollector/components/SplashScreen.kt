package com.example.quotecollector.components

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.R
import com.example.quotecollector.SignUp
import com.example.quotecollector.SignUpPage
import com.example.quotecollector.ui.theme.ItaliannoFont
import com.example.quotecollector.ui.theme.White

@Composable
fun SplashScreen() {
    val currentContext = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
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
                modifier = Modifier
                    .weight(1f, fill = false)
                    .size(280.dp)
            )


            Spacer(modifier = Modifier.height(if (isLandscape) 40.dp else 200.dp))


            CustomButton(
                text = "Get Started",
                onClick ={
                    val intent= Intent(currentContext, SignUp::class.java)
                    currentContext.startActivity(intent)
                },
                width = Int.MAX_VALUE,
                modifier = Modifier.fillMaxWidth()


            )
        }
    }
}