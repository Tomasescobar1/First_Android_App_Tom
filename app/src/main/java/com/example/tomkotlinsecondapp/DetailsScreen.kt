package com.example.tomkotlinsecondapp

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable fun DetailsScreen(onNavigateBack: () -> Unit)
{
    Box(modifier = Modifier.background(Color.Cyan))
    {
        Text(text = "Hello, this is the details screen.")
    }
}
