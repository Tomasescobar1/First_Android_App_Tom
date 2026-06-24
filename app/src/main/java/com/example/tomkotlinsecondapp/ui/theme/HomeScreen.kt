package com.example.tomkotlinsecondapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tomkotlinsecondapp.GuitarOrder
import com.example.tomkotlinsecondapp.GuitarViewPort

@Composable fun HomeScreen(guitarViewModel: GuitarOrder = viewModel())
{
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Transparent).zIndex(2f)
            .clickable(interactionSource = null, indication = null){ guitarViewModel.updateDataState(7, " ", 0.0) },
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .padding(top = 70.dp, bottom = 70.dp).zIndex(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            GuitarViewPort()
        }
    }
}
