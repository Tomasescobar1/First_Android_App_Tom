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

@Composable
fun DropDownSection()
{
    var dropped by remember {mutableStateOf(false)}

    var scaleLengthDropped by remember {mutableStateOf( false)}

    var selectedAxe by remember {mutableStateOf("None")}

    val savedGuitar = Guitar()

    savedGuitar.model = modelIndVal.value

    val guitarNames = listOf("Telecaster", "Growler")

    val scaleLengths = listOf(25.5, 25.0, 24.75, 24.0)

    //val guitarNames1 = listOf("Telecaster", "Stratocaster", "Jaguar", "Jazzmaster", "Growler")

    Box(modifier = Modifier.width(200.dp).height(80.dp)//.padding(bottom = 8.dp)
        .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center)
    {

        TextButton(onClick = {dropped =  true}, modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp)) {
            Text(text = modelIndVal.value, color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }

        DropdownMenu(
            expanded = dropped,
            onDismissRequest = {dropped = false},
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            guitarNames.forEach { guitarName ->
                DropdownMenuItem(
                    text = {Text(guitarName, color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                    onClick = {
                        modelIndVal.value =  guitarName
                        dropped = false
                    },
                    //contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                    modifier = Modifier.width(200.dp).background(color = Color.Transparent, RoundedCornerShape(16.dp))
                )
            }
        }
    }

    Box(modifier = Modifier.width(200.dp).height(80.dp)
        .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center)
    {
        TextButton(onClick = {scaleLengthDropped = true}, modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp)) {
            Text(text = "Scale Length", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }

        DropdownMenu(
            expanded = scaleLengthDropped,
            onDismissRequest = {scaleLengthDropped = false},
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            scaleLengths.forEach { scaleLength ->
                DropdownMenuItem(
                    text = {Text(scaleLength.toString(), color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                    onClick = {
                        scaleLengthInd.doubleValue = scaleLength
                        scaleLengthDropped = false
                    },

                    modifier = Modifier.background(Color.Transparent, RoundedCornerShape(16.dp)).width(200.dp)
                )
            }
        }
    }

    Box(modifier = Modifier.width(200.dp).height(80.dp)
        .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center)
    {
        TextButton(onClick = {}, modifier = Modifier.background(Color.White,RoundedCornerShape(12.dp)).width(150.dp))  {
            Text(text = "Place Order", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }

}