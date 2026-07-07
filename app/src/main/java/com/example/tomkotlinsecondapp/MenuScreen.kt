package com.example.tomkotlinsecondapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable fun MenuScreen(onNavigateToMain: () -> Unit)
{
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top = 200.dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Box(modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)).width(200.dp).height(50.dp))
        {
            Text(
                text = "Hello, this is the menu screen.",
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Box(modifier = Modifier.height(200.dp).width(200.dp))

        Box(
            modifier = Modifier.width(200.dp).height(80.dp)
                .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        )
        {
            TextButton(
                onClick = onNavigateToMain,
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    .width(150.dp)
            ) {
                Text(
                    text = "Go to main screen",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
