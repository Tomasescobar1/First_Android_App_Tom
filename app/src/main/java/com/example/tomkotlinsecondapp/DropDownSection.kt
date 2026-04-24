package com.example.tomkotlinsecondapp

import android.text.style.LineHeightSpan
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun DropDownSection(guitarViewModel: GuitarOrder = viewModel())
{

    val orderState by guitarViewModel.orderState.collectAsStateWithLifecycle()

    val cDataState by guitarViewModel.dataState.collectAsStateWithLifecycle()

    var dropped by remember {mutableStateOf(false)}

    var scaleLengthDropped by remember {mutableStateOf( false)}

    val guitarNames = listOf("Telecaster", "Growler")

    val scaleLengths = listOf(25.5, 25.0, 24.75, 24.0)

    Box(
        modifier = Modifier.width(200.dp).height(80.dp)//.padding(bottom = 8.dp)
            .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    )
    {
        TextButton(
            onClick = { dropped = true },
            modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp)
        ) {
            Text(
                text = cDataState.modelIndVal,
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        DropdownMenu(
            expanded = dropped,
            onDismissRequest = { dropped = false },
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            guitarNames.forEach { guitarName ->
                DropdownMenuItem(
                    text = {
                        Text(
                            "Model: $guitarName",
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        ) },
                    onClick = {
                        guitarViewModel.updateDataState(2, guitarName, 0.0)
                        dropped = false },
                    //contentPadding = MenuDefaults.DropdownMenuItemContentPadding,
                    modifier = Modifier.width(200.dp)
                        .background(color = Color.Transparent, RoundedCornerShape(16.dp))
                )
            }
        }
    }
    Box(
        modifier = Modifier.width(200.dp).height(80.dp)
            .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    )
    {
        TextButton(
            onClick = { scaleLengthDropped = true },
            modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp)
        ) {
            Text(
                text = "Scale Length: ${cDataState.scaleLengthInd} in.",
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        DropdownMenu(
            expanded = scaleLengthDropped,
            onDismissRequest = { scaleLengthDropped = false },
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            scaleLengths.forEach { scaleLength ->
                DropdownMenuItem(
                    text = {
                        Text(
                            scaleLength.toString() + "in.",
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        ) },
                    onClick = {
                        guitarViewModel.updateDataState(4, " ", scaleLength)
                        scaleLengthDropped = false },
                    modifier = Modifier.background(Color.Transparent, RoundedCornerShape(16.dp))
                        .width(200.dp)
                )
            }
        }
    }
    if(orderState.instanceInd < 5)
    {
        Box(
            modifier = Modifier.width(200.dp).height(80.dp)
                .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        )
        {
            TextButton(
                onClick = { guitarViewModel.updateOrderState(2, true) },
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    .width(150.dp)
            ) {
                Text(
                    text = "Place Order",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    else
    {
        Box(
            modifier = Modifier.width(200.dp).height(80.dp)
                .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        )
        {
            TextButton(
                onClick = { guitarViewModel.updateOrderState(1,true) },
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    .width(150.dp)
            ) {
                Text(
                    text = "View Order List",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}