package com.example.tomkotlinsecondapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun SpacerButton()
{
    TextButton(
        onClick = { spacerInd.value = !spacerInd.value },
        modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
            .background(Color.Blue, RoundedCornerShape(16.dp))
    )
    {
        Text(
            text = "Confirm",
            color = Color(0xffffffff),
            fontFamily = FontFamily.Monospace
        )
    }
}