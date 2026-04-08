package com.example.tomkotlinsecondapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun FABComponent() {

    var isDeployed by remember {mutableStateOf(false)}

    var fabOffset by remember {mutableStateOf(Offset(0f, 0f))}

    var colorOffset by remember {mutableStateOf(Color(66, 203, 240))}

    fun containerBackground()
    {
        isDeployed = !isDeployed

        if(isDeployed)
        {
            colorOffset = Color.Transparent
        }

        else colorOffset = Color(66, 203, 240)
    }

    FloatingActionButton (
        onClick = { containerBackground() },
        modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).zIndex(3f)
            .offset { IntOffset(fabOffset.x.roundToInt(), fabOffset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    fabOffset = fabOffset.plus(dragAmount)
                }
            },
        containerColor = (colorOffset)
    )
    {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.Black
        )

        AnimatedVisibility(visible = isDeployed)
        {
            Column(
                modifier = Modifier.zIndex(4f).width(200.dp).height(400.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            )
            {
                ColorDropDown()

                DropDownSection()
            }
        }
    }
}