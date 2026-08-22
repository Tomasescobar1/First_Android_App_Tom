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
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun DropDownSection(guitarViewModel: GuitarOrder)
{

    data class LocalStates(
        val dropped: Boolean = false,
        val findTextBoxInd: Boolean = false,
        val scaleLengthDropped: Boolean = false,
        val searchLoadTrigger: Boolean = false,
        val loadingUpdateTrigger: Boolean = false,
        val orderFoundInd: Boolean = false,
        val orderFindFail: Boolean = false,
        val orderFindFailInd: Boolean = false,
        val orderUpdateInd: Boolean = false,
        val orderUpdateFail: Boolean = false,
        val orderModInd: Boolean = false,
        val orderDeleteConfirm: Boolean = false,
    )

    var localStates by remember {mutableStateOf(LocalStates())}

    val orderState by guitarViewModel.orderState.collectAsStateWithLifecycle()

    val cDataState by guitarViewModel.dataState.collectAsStateWithLifecycle()

    val foundOrderString by guitarViewModel.foundOrderString.collectAsStateWithLifecycle()

    val updatedOrderString by guitarViewModel.updatedOrderString.collectAsStateWithLifecycle()

    val offlineState by guitarViewModel.isOffline.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var customerInputLocal by remember {mutableStateOf("")}

    val guitarNames = listOf("Telecaster", "Growler")

    val scaleLengths = listOf(25.5, 25.0, 24.75, 24.0)

    fun dialogDismiss(input:Boolean = false)
    {
        localStates = localStates.copy(findTextBoxInd = false)

        customerInputLocal = ""

        localStates = localStates.copy(orderFoundInd = false)

        if(input)
        {
            guitarViewModel.updateOrderState(7, false)
        }

        guitarViewModel.updateOrderState(6, false)

        guitarViewModel.updateOrderState(4, false)
    }

    LaunchedEffect(localStates.loadingUpdateTrigger)
    {
        if(localStates.loadingUpdateTrigger)
        {
            guitarViewModel.orderUpdate(customerInputLocal, cDataState.modelIndVal,
                cDataState.colorInput, cDataState.scaleLengthInd)

            customerInputLocal = ""

            delay(1500L.milliseconds)

            if(orderState.updateSuccess)
            {
                localStates = localStates.copy(loadingUpdateTrigger = false)

                guitarViewModel.updateOrderState(8, false)

                localStates = localStates.copy(orderUpdateInd = true)
            }
        }
    }

    LaunchedEffect(localStates.orderUpdateInd)
    {
        if(localStates.orderUpdateInd)
        {
            delay(3000L.milliseconds)

            guitarViewModel.updateOrderState(7, true)

            localStates = localStates.copy(orderUpdateInd = false)

            localStates = localStates.copy(orderModInd = false)
        }
    }

    LaunchedEffect(orderState.orderDelete)
    {
        if(orderState.orderDelete)
        {
            delay(3000L.milliseconds)

            guitarViewModel.updateOrderState(9, false)

            guitarViewModel.updateOrderState(6, false)

            localStates = localStates.copy(orderDeleteConfirm = false)

            localStates = localStates.copy(findTextBoxInd = false)

            localStates = localStates.copy(orderFoundInd = false)
        }
    }

    LaunchedEffect(orderState.orderSearchLoad)
    {
        if(orderState.orderSearchLoad)
        {
            localStates = localStates.copy(searchLoadTrigger = true)

            delay(3000L.milliseconds)

            localStates = localStates.copy(searchLoadTrigger = false)

            guitarViewModel.updateOrderState(10, false)

            if(orderState.orderFoundInd)
            {
                localStates = localStates.copy(orderFoundInd = true)
            }
            else if(orderState.orderFoundFail)
            {
                localStates = localStates.copy(orderFindFailInd = true)
            }
        }
    }

    LaunchedEffect(localStates.orderFindFailInd)
    {
        if(orderState.orderFoundFail && localStates.orderFindFailInd)
        {
            localStates = localStates.copy(orderFindFail = true)

            delay(1500L.milliseconds)

            guitarViewModel.updateOrderState(6, true)

            localStates = localStates.copy(orderFindFail = false)

            localStates= localStates.copy(orderFindFailInd = false)

            localStates = localStates.copy(findTextBoxInd = false)

            customerInputLocal = ""
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
            onClick = { localStates = localStates.copy(dropped = true) },
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
            expanded = localStates.dropped,
            onDismissRequest = { localStates = localStates.copy(dropped = false) },
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White)
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
                        localStates = localStates.copy(dropped = false) },
                    modifier = Modifier.background(Color.Transparent).width(200.dp)
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
            onClick = { localStates = localStates.copy(scaleLengthDropped = true) },
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
            expanded = localStates.scaleLengthDropped,
            onDismissRequest = { localStates = localStates.copy(scaleLengthDropped = false) },
            modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White)
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
                        localStates = localStates.copy(scaleLengthDropped = false) },
                    modifier = Modifier.background(Color.Transparent).width(200.dp)
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
            onClick = { localStates = localStates.copy(findTextBoxInd = true) },
            enabled = !offlineState && orderState.credentialToggleInput,
            modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                .width(150.dp)
        ) {
            Text(
                text = "Find your orders",
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if(localStates.findTextBoxInd)
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
                        if(localStates.searchLoadTrigger)
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
                }

            },
            confirmButton = {}
        )
    }

    if(localStates.orderFoundInd)
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
                        onClick = { localStates = localStates.copy(orderDeleteConfirm = true) },
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

    if(localStates.orderModInd)
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
                        if(localStates.loadingUpdateTrigger)
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
                                onClick = { localStates = localStates.copy(loadingUpdateTrigger = true) },
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

    if(localStates.orderDeleteConfirm)
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

    if(localStates.orderUpdateInd)
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

    if(localStates.orderFindFail)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Order not found!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {
                if(orderState.orderFoundFailMode)
                {
                    Text(text = "Failed to connect to server.")
                }
            }
            },
            confirmButton = {})
    }

    if(localStates.orderUpdateFail)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Failed to connect to server and update order!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
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
                    onClick = {
                        if(orderState.credentialToggleInput)
                        {
                            guitarViewModel.updateOrderState(2, true)
                        }
                        else
                        {
                            guitarViewModel.signInWithGoogle(context)
                        }
                              },
                    enabled = !offlineState,
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
                    onClick = { localStates = localStates.copy(orderModInd = true) },
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