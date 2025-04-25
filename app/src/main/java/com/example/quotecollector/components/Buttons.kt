package com.example.quotecollector.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.White

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    width: Int = 230,
    height: Int = 50,
    fontSize: Int = 16,
    modifier: Modifier = Modifier
) {
    val buttonShape = RoundedCornerShape(4.dp)

    // Create base modifier with height
    val buttonModifier = if (width == Int.MAX_VALUE) {
        modifier.fillMaxWidth().height(height.dp)
    } else {
        modifier.width(width.dp).height(height.dp)
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Color.Black
        ),
        shape = buttonShape,
        modifier = buttonModifier
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Poppins
        )
    }
}