package com.example.tomkotlinsecondapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun cameraToggle()
{
    cameraInd.intValue ++

    if(cameraInd.intValue > 3)
    {
        cameraInd.intValue = 0
    }
}

@Composable
fun ColorDropDown()
{
    var dropped by remember {mutableStateOf(false)}

    val guitarColors = listOf("White", "Red", "Sky Blue", "Olive Green")

    Box(modifier = Modifier.width(200.dp).height(80.dp)//.padding(start = 8.dp)
        .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center)
    {
        TextButton(onClick = {cameraToggle()}, modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp)) {
            Text(text = "Camera Toggle", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }

    Box(modifier = Modifier.width(200.dp).height(80.dp)//.padding(start = 8.dp)
        .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center)
    {

        TextButton(onClick = {dropped =  true}, modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp)) {
            Text(text = "Color: ${colorInput.value}", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }

        DropdownMenu(
            expanded = dropped,
            onDismissRequest = {dropped = false},
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            guitarColors.forEach { guitarColor ->
                DropdownMenuItem(
                    text = {Text(guitarColor, color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                    onClick = {
                        colorInput.value =  guitarColor
                        dropped = false

                    },
                    //contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }

}