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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.delay

@Composable
fun DropDownSection(guitarViewModel: GuitarOrder = viewModel())
{

    val orderState by guitarViewModel.orderState.collectAsStateWithLifecycle()

    val cDataState by guitarViewModel.dataState.collectAsStateWithLifecycle()

    val foundOrderString by guitarViewModel.foundOrderString.collectAsStateWithLifecycle()

    val updatedOrderString by guitarViewModel.updatedOrderString.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var dropped by remember {mutableStateOf(false)}

    var findTextBoxInd by remember {mutableStateOf(false)}

    var scaleLengthDropped by remember {mutableStateOf( false)}

    var customerInputLocal by  remember{mutableStateOf("")}

    var loadingTrigger by remember {mutableStateOf(false)}

    var orderFindFail by remember{mutableStateOf(false)}

    var orderUpdateFail by remember{mutableStateOf(false)}

    var orderModInd by remember{mutableStateOf(false)}

    var orderDeleteConfirm by remember{mutableStateOf(false)}

    var orderUpdateInd by remember{mutableStateOf(false)}

    val guitarNames = listOf("Telecaster", "Growler")

    val scaleLengths = listOf(25.5, 25.0, 24.75, 24.0)

    fun dialogDismiss(input:Boolean = false)
    {
        findTextBoxInd = false

        customerInputLocal = ""

        if(input)
        {
            guitarViewModel.updateOrderState(7, false)
        }

        guitarViewModel.updateOrderState(6, false)

        guitarViewModel.updateOrderState(4, false)
    }

    LaunchedEffect(loadingTrigger)
    {
        if(loadingTrigger)
        {
            guitarViewModel.orderUpdate(customerInputLocal, cDataState.modelIndVal,
                cDataState.colorInput, cDataState.scaleLengthInd)

            delay(1500L)

            if(orderState.updateSuccess)
            {
                loadingTrigger = false

                guitarViewModel.updateOrderState(8, false)

                orderUpdateInd = true
            }
        }
    }

    LaunchedEffect(orderUpdateInd)
    {
        if(orderUpdateInd)
        {
            delay(3000L)

            orderUpdateInd = false

            orderModInd = false
        }
    }

    LaunchedEffect(orderState.orderDelete)
    {
        if(orderState.orderDelete)
        {
            delay(3000L)

            guitarViewModel.updateOrderState(9, false)

            guitarViewModel.updateOrderState(6, false)

            orderDeleteConfirm = false

            findTextBoxInd = false
        }
    }

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

    if(orderState.instanceInd > 0)
    {


        Box(
            modifier = Modifier.width(200.dp).height(80.dp)
                .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        )
        {
            TextButton(
                onClick = { findTextBoxInd = true },
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    .width(150.dp)
            ) {
                Text(
                    text = "Find your order",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if(findTextBoxInd)
    {
        AlertDialog(
            onDismissRequest = {  },
            title = {},
            text = {
                Column(
                    modifier = Modifier.height(400.dp).width(400.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Please type in your name to find your order.",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(

                        value = customerInputLocal,
                        onValueChange = { customerInputLocal = it },
                        label = {
                            Text(
                                text = "Your name here.",
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions (
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        )
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
                            onClick = { guitarViewModel.orderFind(customerInputLocal) },
                            modifier = Modifier.background(
                                Color.White,
                                RoundedCornerShape(12.dp)
                            )
                                .width(150.dp)
                        ) {
                            Text(
                                text = "Find Order",
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

    if(orderState.orderFoundInd)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Order found!" + "\nSpecifications: ", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {
                Text(
                    text = foundOrderString, overflow = TextOverflow.Clip,
                    lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                Box(
                    Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(170.dp).height(55.dp)
                        .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                )
                {
                    TextButton(
                        onClick = {dialogDismiss(true)},
                        modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                            .width(150.dp).height(35.dp)
                    )
                    {
                        Text("Modify order",
                            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                }

                Box(modifier = Modifier.height(30.dp).width(80.dp))

                Box(
                    Modifier.background(Color(245, 66, 87), RoundedCornerShape(10.dp)).width(170.dp).height(55.dp)
                        .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                )
                {
                    TextButton(
                        onClick = { orderDeleteConfirm = true },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                            .width(150.dp).height(35.dp)
                    )
                    {
                        Text("Delete order",
                            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                }

            }
            },
            confirmButton = {
                Box(
                    Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(120.dp).height(55.dp)
                        .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                )
                {
                    TextButton(
                        onClick = {dialogDismiss()},
                        modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                            .width(90.dp).height(35.dp)
                    )
                    {
                        Text("Confirm",
                            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                }
            })
    }

    if(orderModInd)
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
                        text = "Please type in your name to modify the order.",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(

                        value = customerInputLocal,
                        onValueChange = { customerInputLocal = it },
                        label = {
                            Text(
                                text = "Your name here.",
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions (
                            onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Box(modifier = Modifier.height(30.dp).width(80.dp))

                    Box(
                        modifier = Modifier.width(200.dp).height(80.dp)
                            .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    )
                    {
                        if(loadingTrigger)
                        {
                            Box(modifier = Modifier.height(50.dp).width(150.dp).background(Color.White, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center)
                            {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(40.dp),
                                    strokeWidth = 4.dp, color = Color.White,
                                    trackColor = Color(66, 203,245)
                                )
                            }
                        }
                        else
                        {
                            TextButton(
                                onClick = { loadingTrigger = true },
                                modifier = Modifier.background(
                                    Color.White,
                                    RoundedCornerShape(12.dp)
                                )
                                    .width(150.dp)
                            ) {
                                Text(
                                    text = "Confirm",
                                    color = Color.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }
                }

            },
            confirmButton = {}
        )
    }

    if(orderDeleteConfirm)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Confirm order delete?", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {
                Box(
                    modifier = Modifier.width(200.dp).height(80.dp)
                        .background(Color(245, 66, 87), RoundedCornerShape(16.dp))
                        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                )
                {
                    TextButton(
                        onClick = { guitarViewModel.orderDelete() },
                        modifier = Modifier.background(
                            Color.White,
                            RoundedCornerShape(12.dp)
                        )
                            .width(150.dp)
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            },
            confirmButton = {})
    }

    if(orderState.orderDelete)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Order deleted successfully!" + "\n Specs:", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {
                Text(
                    text = foundOrderString, overflow = TextOverflow.Clip,
                    lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
            },
            confirmButton = {})
    }

    if(orderUpdateInd)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Order updated!" + "\n Specifications:", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {
                Text(
                    text = updatedOrderString, overflow = TextOverflow.Clip,
                    lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
            },
            confirmButton = {})
    }

    if(orderFindFail)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Order not found!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {

            }
            },
            confirmButton = {})
    }

    if(orderUpdateFail)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Failed to update order!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {

            }
            },
            confirmButton = {})
    }


    if(orderState.instanceInd < 5)
    {
        if(orderState.orderUpdate)
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
                    onClick = { orderModInd = true },
                    modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                        .width(150.dp)
                ) {
                    Text(
                        text = "Update Order",
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
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