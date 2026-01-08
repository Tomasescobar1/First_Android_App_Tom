package com.example.tomkotlinsecondapp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
//import androidx.compose.material3

@Composable
fun GuitarViewPort()
{
    Box(modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(24.dp)).height(300.dp).width(200.dp))
    {
        Text(
            text = "This is going to be the guitar viewport"
        )
    }
}