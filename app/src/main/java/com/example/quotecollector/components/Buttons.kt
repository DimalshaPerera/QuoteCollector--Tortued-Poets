package com.example.quotecollector.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quotecollector.ui.theme.DarkGray
import com.example.quotecollector.ui.theme.Poppins
import com.example.quotecollector.ui.theme.White


@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    width: Int = 230,
    height: Int = 60,
    isGradient: Boolean = false,
    fontSize: Int = 20,
    modifier: Modifier = Modifier
) {
    val buttonShape = RoundedCornerShape(50)

    if (isGradient) {

        Button(
            onClick = onClick,
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF333333)
            ),
            shape = buttonShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp
            ),
            modifier = modifier
                .height(height.dp)
                .width(width.dp)
                .background(
                    brush = Brush.linearGradient(

                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFE0E0E0)),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    ),
                    shape = buttonShape
                )
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333) // Dark gray text
            )
        }
    } else {

        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = DarkGray
            ),
            shape = buttonShape,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 2.dp
            ),
            modifier = modifier
                .height(height.dp)
                .width(width.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF8F8F8)),
                        start = Offset(0f, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    ),
                    shape = buttonShape
                )
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF555555)
            )
        }
    }
}