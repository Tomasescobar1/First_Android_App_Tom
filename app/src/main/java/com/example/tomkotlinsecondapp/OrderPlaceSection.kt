package com.example.tomkotlinsecondapp

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.zIndex

@Composable
fun ConfirmSection(guitarViewModel: GuitarOrder = viewModel())
{

    var orderConfirm by remember {mutableStateOf(false)}

    val context = LocalContext.current

    val view = LocalView.current

    //var orderPlace by remember { mutableStateOf(false)}

    fun confirmData()
    {
        guitarViewModel.addListElement(
            customerInputVal.value,
            modelIndVal.value,
            colorInput.value,
            scaleLengthInd.doubleValue
        )
        for (i in 0 until guitarViewModel.orderList.size)
        {
            println(guitarViewModel.orderList[i].customer)
        }

        orderConfirm = true

        instanceInd.intValue ++
    }

    LaunchedEffect(orderConfirm)
    {

        delay(6000L)

        orderConfirm = false

        orderPlaceG.value = false

    }

    if(orderPlaceG.value)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {},
            text = {
                Column(
                    modifier = Modifier.height(400.dp).width(400.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please type in your name to place the order.",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = customerInputVal.value,
                        onValueChange = { customerInputVal.value = it },
                        label = {
                            Text(
                                text = "Your name here.",
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true
                    )

                    Box(modifier = Modifier.height(30.dp).width(80.dp))

                    Box(
                        modifier = Modifier.width(200.dp).height(80.dp)
                            .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    )
                    {
                        TextButton(
                            onClick = { confirmData() },
                            modifier = Modifier.background(
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
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

            },
            confirmButton = {}
        )
    }

    if(orderConfirm)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Order confirmed!" + "\nSpecifications: ", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Text(text = " Customer name: ${guitarViewModel.orderList.last().customer}" +
                    "\n Model: ${guitarViewModel.orderList.last().model}" +
                    "\n Color: ${guitarViewModel.orderList.last().color}" +
                    "\n Scale length: ${guitarViewModel.orderList.last().scaleLength} in ", overflow = TextOverflow.Clip,
                lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            confirmButton = {})
    }
}
