package com.example.tomkotlinsecondapp

import android.text.style.LineHeightSpan
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownSection(guitarViewModel: GuitarOrder = viewModel())
{
    var dropped by remember {mutableStateOf(false)}

    var scaleLengthDropped by remember {mutableStateOf( false)}

    var orderConfirm by remember {mutableStateOf(false)}

    var orderPlace by remember {mutableStateOf(false)}

    var instanceInd by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current

    val guitarNames = listOf("Telecaster", "Growler")

    val scaleLengths = listOf(25.5, 25.0, 24.75, 24.0)

    fun confirmData()
    {
        guitarViewModel.addListElement(customerInputVal.value, modelIndVal.value, colorInput.value, scaleLengthInd.doubleValue)

        for(i in 0 until guitarViewModel.orderList.size)
        {
            println(guitarViewModel.orderList[i].color)
        }

        orderConfirm = true
    }

    LaunchedEffect(orderConfirm)
    {
        delay(6000L)

        orderConfirm = false

        orderPlace = false
    }

    if(orderPlace)
    {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color.Black),
            onDismissRequest =  {orderPlace = false},
            title = {Text(text = "One last thing...", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = { Column(modifier = Modifier.height(400.dp).width(400.dp), verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Please type in your name to place the order.", fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = customerInputVal.value,
                                onValueChange = {customerInputVal.value = it},
                                label = {Text(text = "Your name here.", fontFamily = FontFamily.Monospace)},
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),
                                singleLine = true
                            )

                            Box(modifier = Modifier.height(30.dp).width(80.dp))

                            Box(modifier = Modifier.width(200.dp).height(80.dp)
                                .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center)
                            {
                                TextButton(onClick = {confirmData()}, modifier = Modifier.background(Color.White,RoundedCornerShape(12.dp)).width(150.dp))  {
                                    Text(text = "Place Order", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                   },
            confirmButton = {}

        )
    }

    if(orderConfirm)
    {
        AlertDialog(
            onDismissRequest = {orderConfirm = false},
            title = {Text("Order confirmed!" + "\nSpecifications: ", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Text(text = " Customer name: ${guitarViewModel.orderList.last().customer}" +
                    "\n Model: ${guitarViewModel.orderList.last().model}" +
                    "\n Color: ${guitarViewModel.orderList.last().color}" +
                    "\n Scale length: ${guitarViewModel.orderList.last().scaleLength} in ", overflow = TextOverflow.Clip,
                    lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            confirmButton = {})
    }

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
                    text = {Text("Model: $guitarName", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
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
            Text(text = "Scale Length: ${scaleLengthInd.doubleValue}", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
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
        TextButton(onClick = {orderPlace = true}, modifier = Modifier.background(Color.White,RoundedCornerShape(12.dp)).width(150.dp))  {
            Text(text = "Place Order", color = Color.Black, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }

}