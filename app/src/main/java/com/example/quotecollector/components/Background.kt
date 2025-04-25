package com.example.quotecollector.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.quotecollector.R


@Composable
fun Background(){
    val backgroundImage= painterResource(id= R.drawable.dark)
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Image(
            painter = backgroundImage,
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),

            contentScale = ContentScale.FillBounds
        )
    }

}