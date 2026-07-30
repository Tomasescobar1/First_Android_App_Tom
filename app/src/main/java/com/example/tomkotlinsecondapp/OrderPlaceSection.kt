package com.example.tomkotlinsecondapp

import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.collections.get
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ConfirmSection(guitarViewModel: GuitarOrder = viewModel())
{

    data class LocalStateClass (
        val loadingTrigger: Boolean = false,
        val orderLoadSuccess: Boolean = false,
        val orderRemove: Boolean = false,
        val orderListFinal: Boolean = false
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val orderState by guitarViewModel.orderState.collectAsStateWithLifecycle()

    val cDataState by guitarViewModel.dataState.collectAsStateWithLifecycle()

    val loadingState by guitarViewModel.isLoading.collectAsStateWithLifecycle()

    var customerInputLocal by remember {mutableStateOf("")}

    var localStateManager by remember {mutableStateOf(LocalStateClass())}

    fun confirmData()
    {
        if(customerInputLocal != "")
        {
            guitarViewModel.addListElement(
                customerInputLocal,
                cDataState.modelIndVal,
                cDataState.colorInput,
                cDataState.scaleLengthInd
            )
            for (i in 0 until guitarViewModel.orderList.size) {
                println(guitarViewModel.orderList[i].customer)
            }
        }
    }

    fun dialogDismiss(input: Boolean = true, input2: Boolean)
    {
        if(input && input2)
        {
            guitarViewModel.updateOrderState(2, false)

            localStateManager = localStateManager.copy(orderLoadSuccess = false)

            customerInputLocal = ""
        }

        else if(input)
        {
            guitarViewModel.updateOrderState(2, false)

            guitarViewModel.updateOrderState(5, false)
        }

        else
        {
            guitarViewModel.updateOrderState(1, false)

            localStateManager = localStateManager.copy(orderListFinal = false)
        }
    }

    fun listReset()
    {
        guitarViewModel.updateOrderState(1, false)

        guitarViewModel.orderList.clear()

        guitarViewModel.updateOrderState(3, false)

        localStateManager = localStateManager.copy(orderRemove = true)
    }

    LaunchedEffect(loadingState)
    {
        if(loadingState)
        {
            confirmData()

            localStateManager = localStateManager.copy(loadingTrigger = true)

            guitarViewModel.addDataToFirestore(inputOrderData = guitarViewModel.dbOrderList)

            delay(2000L.milliseconds)

            if(orderState.orderSuccess)
            {
                localStateManager = localStateManager.copy(orderLoadSuccess = true)
            }

            guitarViewModel.updateOrderState(4, false)

            localStateManager = localStateManager.copy(loadingTrigger = false)
        }
    }

    LaunchedEffect(localStateManager.orderRemove)
    {
        delay(3000L.milliseconds)

        localStateManager = localStateManager.copy(orderRemove = false)

        localStateManager = localStateManager.copy(orderListFinal = false)
    }

    if(orderState.orderPlaceG)
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
                        if(!localStateManager.loadingTrigger)
                        {
                            TextButton(
                                onClick = { guitarViewModel.updateOrderState(4, true) },
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
                        else
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
                    }
                }

            },
            confirmButton = {}
        )
    }

    if(localStateManager.orderLoadSuccess)
    {
        AlertDialog(
            onDismissRequest = {dialogDismiss(input2 = true)},
            title = {Text("Order confirmed!" + "\nSpecifications: ", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
                    {
                        Text(
                            text = " Customer name: ${guitarViewModel.orderList.last().customer}" +
                            "\n Model: ${guitarViewModel.orderList.last().model}" +
                            "\n Color: ${guitarViewModel.orderList.last().color}" +
                            "\n Scale length: ${guitarViewModel.orderList.last().scaleLength} in ", overflow = TextOverflow.Clip,
                        lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                        for (i in 0 until guitarViewModel.orderList.size) {
                            Text(text = "\nCustomer ${i+1}: ${guitarViewModel.orderList[i].customer}",
                                lineHeight = 25.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }

                        Text("\nAvailable order slots: ${5 - orderState.instanceInd}", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                    }
                   },
            confirmButton = {
                        Box(
                            Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(120.dp).height(55.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                        )
                        {
                        TextButton(
                                onClick = {dialogDismiss(input2 = true)},
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

    if(orderState.orderFail)
    {
        AlertDialog(
            onDismissRequest = {dialogDismiss(true, false)},
            title = {Text("Failed to place order!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {

            }
            },
            confirmButton = {
                Box(
                    Modifier.background(Color(245,66,87), RoundedCornerShape(10.dp)).height(55.dp).width(120.dp)
                        .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center)
                {
                    TextButton(
                        onClick = {dialogDismiss(true, false)},
                        modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                            .height(35.dp).width(90.dp)
                    )
                    {
                        Text("Confirm",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                }
            })
    }

    if(orderState.orderListFull)
    {
        AlertDialog(
            onDismissRequest = {guitarViewModel.updateOrderState(1,false)},
            title = {Text("Order slots full!" + "\nCustomer list: ", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
            {

                for (i in 0 until guitarViewModel.orderList.size) {
                    Text(
                        text = "\nCustomer ${i+1}: ${guitarViewModel.orderList[i].customer}",
                        lineHeight = 25.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold
                    )
                }

            }
            },
            confirmButton = {
                Box(
                    Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).height(55.dp).width(120.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center)
                {
                TextButton(
                    onClick = { localStateManager = localStateManager.copy(orderListFinal = true) },
                    modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                        .height(35.dp).width(90.dp)
                    )
                    {
                        Text("Confirm",
                            fontWeight = FontWeight.Bold,
                        color = Color.Black)
                    }
                }
            })
    }

    if(localStateManager.orderListFinal)
    {
        AlertDialog(
            onDismissRequest = {dialogDismiss(false, false)},
            title = {Text("Do you want to clear the list (local action)?",
                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally)
            {

                Text("\nTo clear, press the red button, otherwise, press exit at the bottom:", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                Box(modifier = Modifier.height(20.dp).width(40.dp))

                Box(
                    modifier = Modifier.width(200.dp).height(80.dp)
                        .background(Color(245, 66, 87), RoundedCornerShape(16.dp))
                        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { listReset() },
                        modifier = Modifier.background(
                            Color.White,
                            RoundedCornerShape(12.dp)
                        )
                            .width(150.dp)
                    ) {
                        Text(
                            text = "Reset List.",
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            },
            confirmButton = {
                Box(
                    modifier = Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(120.dp).height(55.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center)
                {
                TextButton(
                    onClick = {dialogDismiss(false, false)},
                    modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                        .height(35.dp).width(90.dp)
                )
                    {
                        Text(
                            text = "Exit",
                            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            })
    }

    if(localStateManager.orderRemove)
    {
        AlertDialog(
            onDismissRequest = {},
            title = {Text("Orders Removed!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {Column(verticalArrangement = Arrangement.Top)
                {

                }
            },
            confirmButton = {})
    }
}
