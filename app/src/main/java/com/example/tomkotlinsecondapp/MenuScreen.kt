package com.example.tomkotlinsecondapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/*fun formatDayMonthYear(timeStampMillis: Long): String {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochMilli(timeStampMillis))

}*/

@Composable fun MenuScreen(onNavigateToMain: () -> Unit, guitarViewModel: GuitarOrder)
{

    data class LocalStateClass(
        var depSideBar: Boolean = false,
        var menuLeave: Boolean = false,
        var maintenanceToggle: Boolean = false,
        var maintenanceLoadingTrigger: Boolean = false,
        var maintenanceSuccessLocal: Boolean = false,
        var maintenanceListInd: Boolean = false,
        var maintenanceUpdateInd: Boolean = false,
        var maintenanceDeleteConfirm: Boolean = false,
        var checkListToggle: Boolean = false,
        var nameInputToggle: Boolean = false,
        var nameIsEmpty: Boolean = true,
    )

    data class TrackedValue(
        val createdAt: Long = System.currentTimeMillis()
    )

    var localStateManager by remember {mutableStateOf(LocalStateClass())}

    val keyboardController = LocalSoftwareKeyboardController.current

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    val orderState by guitarViewModel.orderState.collectAsStateWithLifecycle()

    val maintenanceLoading by guitarViewModel.maintenanceLoading.collectAsStateWithLifecycle()

    val authLoadingState by guitarViewModel.authLoadingState.collectAsStateWithLifecycle()

    val authState by guitarViewModel.authState.collectAsStateWithLifecycle()

    val maintenancePlaced by guitarViewModel.userMaintenanceView.collectAsStateWithLifecycle()

    val localDateIndicator by guitarViewModel.fetchedMaintenanceDate.collectAsStateWithLifecycle()

    val offlineState by guitarViewModel.isOffline.collectAsStateWithLifecycle()

    val fetchedMaintenance by guitarViewModel.maintenanceSpecs.collectAsStateWithLifecycle()

    val fetchedMaintenanceDate by guitarViewModel.fetchedMaintenanceDate.collectAsStateWithLifecycle()

    val specificMaintenanceDoc by guitarViewModel.specificMaintenanceDoc.collectAsStateWithLifecycle()

    val maintenanceFetchLoad by guitarViewModel.maintenanceFetchLoad.collectAsStateWithLifecycle()

    val specificFetchedMaintenance by guitarViewModel.specificFetchedMaintenance.collectAsStateWithLifecycle()

    val dateStorage by remember {mutableStateOf(TrackedValue())}

    var convertedDate by remember {mutableStateOf("")}

    var nameStorage by remember { mutableStateOf("") }

    val maintenanceMapList = remember { mutableStateMapOf<String, Any>() }

    val maintenanceArray = remember { mutableStateListOf<String>() }

    var colorOffset by remember { mutableStateOf(Color(66, 203, 245)) }

    var buttonSizeOffset by remember { mutableDoubleStateOf(0.0) }

    var offlineSignToggle by remember { mutableDoubleStateOf(100.0) }

    offlineSignToggle = when {
        offlineState -> 0.0
        else -> 100.0
    }

    if(nameStorage != "")
    {
        localStateManager = localStateManager.copy(nameIsEmpty = false)
    }
    else
    {
        localStateManager = localStateManager.copy(nameIsEmpty = true)
    }

    class MaintenanceData(val id: Int, val title: String, val initialChecked: Boolean = false)
    {
        var isChecked by mutableStateOf(initialChecked)
    }

    val maintenanceItems = remember { mutableStateListOf(
        MaintenanceData(1, "Basic maintenance"),
        MaintenanceData(2, "Pickup replacement"),
        MaintenanceData(3, "Fret leveling"),
        MaintenanceData(4, "Re-fret"),
        MaintenanceData(5, "Paintwork")
        )
    }

    @Composable
    fun MaintenanceCheckList()
    {
        LazyColumn {
            items(maintenanceItems, key = { it.id }) {item ->
                Row(modifier = Modifier.fillMaxWidth().clickable{item.isChecked = !item.isChecked},
                    verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = item.isChecked, onCheckedChange = {item.isChecked = it})
                    Text(text = item.title, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    fun maintenanceDateConversion(inputDate: String) :String
    {
        val sanitizedDate = inputDate.replace("/", "-")

        return sanitizedDate
    }

    fun maintenanceTypeConversion()
    {
        if(!localStateManager.maintenanceUpdateInd)
        {
            maintenanceMapList.put("Name", nameStorage.lowercase())

            maintenanceArray.add(nameStorage)
        }
        else
        {
            maintenanceMapList.put("Name", fetchedMaintenance?.get("Name").toString())

            maintenanceArray.add(fetchedMaintenance?.get("Name").toString())
        }

        for(i in 0 until maintenanceItems.size)
        {
            if(i == 1)
            {
                if(!localStateManager.maintenanceUpdateInd)
                {
                    maintenanceMapList.put("Date of creation", guitarViewModel.formatDayMonthYear(dateStorage.createdAt))
                }
                else
                {
                    maintenanceMapList.put("Date of creation", maintenanceDateConversion(fetchedMaintenance?.get("Date of creation").toString()))
                }
            }

            if(maintenanceItems[i].isChecked)
            {
                maintenanceMapList.put((i+1).toString(), maintenanceItems[i].title)

                maintenanceArray.add(maintenanceItems[i].title)
            }
            else
            {
                maintenanceMapList.put((i+1).toString(), " ")

                maintenanceArray.add(" ")
            }
        }
    }

    fun dialogDismiss(input: Boolean = false)
    {
        for(i in 0 until maintenanceItems.size)
        {
            maintenanceItems[i].isChecked = false
        }

        maintenanceArray.clear()

        localStateManager = localStateManager.copy(checkListToggle = false)

        localStateManager = localStateManager.copy(nameInputToggle = false)

        guitarViewModel.updateOrderState(11, true)

        nameStorage = ""

        localStateManager = localStateManager.copy(maintenanceSuccessLocal = false)

        if(input)
        {
            guitarViewModel.updateOrderState(11, false)
        }
    }

    fun maintenanceOrder()
    {
        if(localStateManager.maintenanceUpdateInd)
        {
            maintenanceTypeConversion()

            guitarViewModel.addDataToFirestore(
                inputMaintenanceData = maintenanceMapList,
                serviceOption = true,
                update = true,
                serviceDate = maintenanceDateConversion(fetchedMaintenance?.get("Date of creation").toString()),
                dateUpdate = specificMaintenanceDoc
            )
        }
        else {

            maintenanceTypeConversion()

            guitarViewModel.addDataToFirestore(
                inputMaintenanceData = maintenanceMapList,
                serviceOption = true,
                serviceDate = maintenanceDateConversion(maintenanceMapList["Date of creation"].toString())
            )
        }

    }

    fun emptyListCheck(input: Boolean = false)
    {
        for(i in 0 until maintenanceItems.size)
        {
            if(maintenanceItems[i].isChecked)
            {
                if(!input)
                {
                    localStateManager = localStateManager.copy(nameInputToggle = true)
                }
                else
                {
                    maintenanceOrder()
                }

                break
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top = (100 + offlineSignToggle).dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        if(offlineState)
        {
            Box(
                modifier = Modifier.background(Color(245,66,87), RoundedCornerShape(16.dp))
                    .border(4.dp, Color.Black, RoundedCornerShape(16.dp )).width(250.dp).height(80.dp),
                contentAlignment = Alignment.Center
            )
            {
                Box(modifier = Modifier.width(210.dp).height(50.dp).background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center)
                {
                    Text(
                        text = "Currently Offline.",
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(modifier = Modifier.height(20.dp).width(200.dp))
        }

        Box(
            modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)).width(200.dp)
                .height(50.dp)
        )
        {
            Text(
                text = "Hello, this is the menu screen.",
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Box(modifier = Modifier.height(20.dp).width(200.dp))

        Column(modifier = Modifier.fillMaxSize().background(Color.LightGray),
            verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start)
        {
            Column(modifier = Modifier.width((130 + buttonSizeOffset).dp).fillMaxHeight().background(Color.White),
                verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start)
            {
                Box(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null
                    ) { localStateManager = localStateManager.copy(depSideBar = !localStateManager.depSideBar) }.width((130.0 + buttonSizeOffset).dp).height(130.dp)
                        .border(4.dp, Color.Black)
                        .background(colorOffset)
                        .padding(top = 10.dp, bottom = 10.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Button(
                        onClick = { localStateManager = localStateManager.copy(depSideBar = !localStateManager.depSideBar) },
                        modifier = Modifier.zIndex(2f).width((100.0 + buttonSizeOffset).dp)
                            .height(100.dp)
                            .background(Color.White, RoundedCornerShape(15.dp)),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = colorOffset
                        )
                    )
                    {
                        Icon(
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null
                            ) { localStateManager = localStateManager.copy(depSideBar = !localStateManager.depSideBar) },
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }
                }

                if (localStateManager.depSideBar) {
                    buttonSizeOffset = 70.0
                } else {
                    buttonSizeOffset = 0.0
                }

                LaunchedEffect(localStateManager.maintenanceListInd)
                {
                    if(localStateManager.maintenanceListInd)
                    {
                        guitarViewModel.checkSavedDates(true)
                    }
                }

                AnimatedVisibility(
                    visible = localStateManager.depSideBar,
                    enter = slideInHorizontally(animationSpec = tween(200)) { fullWidth -> -fullWidth },
                    exit = slideOutHorizontally(animationSpec = tween(200)) { fullWidth -> -fullWidth }
                )
                {
                    Column(
                        modifier = Modifier.zIndex(3f).width(200.dp).height(480.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    )
                    {

                        Box(
                            modifier = Modifier.width(200.dp).height(80.dp).zIndex(1f)
                                .background(colorOffset, RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            if(authLoadingState)
                            {
                                Box(modifier = Modifier.height(50.dp).width(150.dp).background(Color.White, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center)
                                {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        strokeWidth = 4.dp, color = Color.White,
                                        trackColor = colorOffset
                                    )
                                }
                            }
                            else
                            {
                                TextButton(
                                    onClick = { guitarViewModel.signInWithGoogle(context) },
                                    enabled = !offlineState && !authState,
                                    modifier = Modifier.background(
                                        Color.White,
                                        RoundedCornerShape(12.dp)
                                    ).height(60.dp).width(150.dp)
                                ) {
                                    if(!authState)
                                    {
                                        Text(
                                            text = "Log In For Maintenance.",
                                            color = Color.Black,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    else
                                    {
                                        Text(
                                            text = "You are logged in.",
                                            color = Color.Black,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = maintenancePlaced,
                            enter = slideInVertically(animationSpec = tween(200)){fullHeight -> -fullHeight},
                            exit = slideOutVertically(animationSpec = tween(200){fullHeight -> fullHeight})
                        )
                        {
                            Box(
                                modifier = Modifier.width(200.dp).height(80.dp).zIndex(1f)
                                    .background(colorOffset, RoundedCornerShape(16.dp))
                                    .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                TextButton(
                                    onClick = {
                                        localStateManager = localStateManager.copy(maintenanceListInd = true)
                                              },
                                    modifier = Modifier.background(
                                        Color.White,
                                        RoundedCornerShape(12.dp)
                                    )
                                        .width(150.dp)
                                ) {
                                    Text(
                                        text = "View maintenance orders",
                                        textAlign = TextAlign.Center,
                                        color = Color.Black,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = authState,
                            enter = slideInVertically(animationSpec = tween(200)){fullHeight -> -fullHeight},
                            exit = slideOutVertically(animationSpec = tween(200){fullHeight -> fullHeight})
                        )
                        {
                            Box(
                                modifier = Modifier.width(200.dp).height(80.dp).zIndex(1f)
                                    .background(colorOffset, RoundedCornerShape(16.dp))
                                    .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                TextButton(
                                    onClick = { localStateManager = localStateManager.copy(checkListToggle = !localStateManager.checkListToggle) },
                                    modifier = Modifier.background(
                                        Color.White,
                                        RoundedCornerShape(12.dp)
                                    )
                                        .width(150.dp)
                                ) {
                                    Text(
                                        text = "Maintenance",
                                        color = Color.Black,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier.width(200.dp).height(80.dp)
                                .background(colorOffset, RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = { localStateManager = localStateManager.copy(menuLeave = true) },
                                modifier = Modifier.background(
                                    Color.White,
                                    RoundedCornerShape(12.dp)
                                )
                                    .width(150.dp)
                            ) {
                                Text(
                                    text = "View 3D Models",
                                    color = Color.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        if(localStateManager.maintenanceListInd)
        {
            AlertDialog(
                onDismissRequest = { localStateManager = localStateManager.copy(maintenanceListInd = false) },
                title = {},
                text = {
                    Column(
                        modifier = Modifier.height(600.dp).width(450.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Placed orders history:",
                            lineHeight = 30.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(420.dp), contentAlignment = Alignment.TopCenter)
                        {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                for (i in 0 until (guitarViewModel.maintenanceDateList?.size ?: 5)) {
                                    Box(
                                        modifier = Modifier.width(200.dp).height(80.dp)
                                            .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                                            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        TextButton(
                                            onClick = { guitarViewModel.readOrderFromFirebase(guitarViewModel.maintenanceDateList?.get(i), serviceOption = true)
                                                guitarViewModel.updateOrderState(16, true, guitarViewModel.maintenanceDateList?.get(i).toString()) },
                                            modifier = Modifier.width(150.dp).height(40.dp).background(Color.White, RoundedCornerShape(10.dp)),
                                        )
                                        {
                                            Text(
                                                text = "${guitarViewModel.maintenanceDateList?.get(i)}",
                                                lineHeight = 25.sp,
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
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
                                onClick = { localStateManager = localStateManager.copy(maintenanceListInd = false) },
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
                confirmButton = {}
            )
        }

        if(maintenanceFetchLoad)
        {
            AlertDialog(
                onDismissRequest = {  },
                title = {Text("Maintenance placed on ${localDateIndicator}:", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                text = {Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally)
                {
                    for(i in 0 until guitarViewModel.fetchedMaintenanceList.size)
                    {
                        Box(
                            Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(170.dp).height(55.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = {

                                    guitarViewModel.readOrderFromFirebase(localDateIndicator, true, guitarViewModel.fetchedMaintenanceList[i], true)
                                    guitarViewModel.updateOrderState(16, false, guitarViewModel.fetchedMaintenanceList[i])
                                },
                                modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                    .width(140.dp).height(35.dp)
                            )
                            {
                                Text(
                                    text = guitarViewModel.fetchedMaintenanceList[i],
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.height(30.dp).width(80.dp))

                    Box(
                        Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(120.dp).height(55.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                    )
                    {
                        TextButton(
                            onClick = { guitarViewModel.updateOrderState(17, false) },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .width(90.dp).height(35.dp)
                        )
                        {
                            Text("Confirm",
                                fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }

                }
                },
                confirmButton = {})
        }

        if(specificFetchedMaintenance)
        {
            AlertDialog(
                onDismissRequest = {guitarViewModel.updateOrderState(18, false)},
                title = {Text("Order specifications: ", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                text = {Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(
                        text = "Customer: ${fetchedMaintenance?.get("Name")}\n" +
                                "Maintenance items placed: \n" +
                                " ${fetchedMaintenance?.get("1") ?: ""} \n" +
                                " ${fetchedMaintenance?.get("2") ?: ""} \n" +
                                " ${fetchedMaintenance?.get("3") ?: ""} \n" +
                                " ${fetchedMaintenance?.get("4") ?: ""} \n" +
                                " ${fetchedMaintenance?.get("5") ?: ""} \n",
                        overflow = TextOverflow.Clip,
                        lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                    Box(modifier = Modifier.fillMaxWidth().height(10.dp))

                    Box(
                        Modifier.background(Color(66, 203, 245), RoundedCornerShape(10.dp)).width(170.dp).height(55.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                    )
                    {
                        TextButton(
                            onClick = {
                                localStateManager = localStateManager.copy(maintenanceUpdateInd = true)
                                localStateManager = localStateManager.copy(checkListToggle = !localStateManager.checkListToggle)
                            },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .width(150.dp).height(35.dp)
                        )
                        {
                            Text("Modify order",
                                fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }

                    Box(modifier = Modifier.height(20.dp).width(80.dp))

                    Box(
                        Modifier.background(Color(245, 66, 87), RoundedCornerShape(10.dp)).width(170.dp).height(55.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                    )
                    {
                        TextButton(
                            onClick = {
                                localStateManager = localStateManager.copy(maintenanceDeleteConfirm = true)
                                      },
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
                            onClick = {guitarViewModel.updateOrderState(18, false)},
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

        if(localStateManager.menuLeave)
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                onDismissRequest = { localStateManager = localStateManager.copy(menuLeave = false) },
                title = {Text("You are about to go to the 3D model viewer...", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                text = {
                    Column( modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally )
                    {
                        Text(
                            text = "Confirm?", overflow = TextOverflow.Clip,
                            lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                        Box(
                            Modifier.padding(top = 10.dp).background(colorOffset, RoundedCornerShape(15.dp)).width(245.dp).height(85.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = { localStateManager = localStateManager.copy(menuLeave = false) },
                                modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                    .width(225.dp).height(65.dp)
                            )
                            {
                                Text("No, stay on this section",
                                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                    color = Color.Black)
                            }
                        }

                        Box(modifier = Modifier.height(30.dp).width(80.dp))

                        Box(
                            Modifier.background(Color(245, 66, 87), RoundedCornerShape(15.dp)).width(245.dp).height(85.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = onNavigateToMain,
                                modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                    .width(225.dp).height(65.dp)
                            )
                            {
                                Text("Yes, go to the viewer",
                                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                    color = Color.Black)
                            }
                        }

                    }
                },
                confirmButton = {}
            )
        }

        if(localStateManager.checkListToggle)
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                onDismissRequest = { dialogDismiss() },
                title = {Text("Instrument maintenance...", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                text = {
                    Column( modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally )
                    {
                        Text(
                            text = "Select your instrument's maintenance requirements..", overflow = TextOverflow.Clip,
                            lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                        MaintenanceCheckList()

                        Box(modifier = Modifier.height(30.dp).width(80.dp))

                        Box(
                            Modifier.padding(top = 10.dp).background(colorOffset, RoundedCornerShape(15.dp)).width(230.dp).height(70.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            if(!maintenanceLoading)
                            {
                                TextButton(
                                    onClick = { emptyListCheck(localStateManager.maintenanceUpdateInd) },
                                    modifier = Modifier.background(
                                        Color.White,
                                        RoundedCornerShape(10.dp)
                                    )
                                        .width(200.dp).height(50.dp)
                                )
                                {
                                    Text(
                                        "Schedule maintenance",
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                            else
                            {
                                Box(modifier = Modifier.height(50.dp).width(200.dp).background(Color.White, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center)
                                {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(40.dp),
                                        strokeWidth = 4.dp, color = Color.White,
                                        trackColor = colorOffset
                                    )
                                }
                            }
                        }

                        Box(modifier = Modifier.height(30.dp).width(40.dp))

                        Box(
                            Modifier.padding(top = 10.dp).background(colorOffset, RoundedCornerShape(15.dp)).width(230.dp).height(70.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = { localStateManager = localStateManager.copy(checkListToggle = false) },
                                modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                    .width(200.dp).height(50.dp)
                            )
                            {
                                Text("Done",
                                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                    color = Color.Black)
                            }
                        }

                    }
                },
                confirmButton = {}
            )
        }

        if(localStateManager.nameInputToggle)
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

                            value = nameStorage,
                            onValueChange = { nameStorage = it },
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
                                .background(colorOffset, RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            if(!maintenanceLoading)
                            {
                                TextButton(
                                    onClick = { maintenanceOrder() },
                                    modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).width(150.dp))
                                {
                                    Text(
                                        text = "Request maintenance",
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
                                        trackColor = colorOffset
                                    )
                                }
                            }
                        }
                    }

                },
                confirmButton = {}
            )
        }

        if(orderState.maintenanceSuccess)
        {
            AlertDialog(
                onDismissRequest = {

                    if(localStateManager.maintenanceUpdateInd)
                    {
                        localStateManager = localStateManager.copy(maintenanceUpdateInd = false)

                        guitarViewModel.updateOrderState(17, false)

                        guitarViewModel.updateOrderState(18, false)
                    }

                    dialogDismiss()
                                   },
                title = {
                    if(!localStateManager.maintenanceUpdateInd) {
                        Text(
                            "Maintenance confirmed!" + "\nDetails: ",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    else
                    {
                        Text(
                            "Maintenance updated!" + "\nDetails: ",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                        },
                text = {Column (verticalArrangement = Arrangement.Top)
                {
                    Text(text = "Name: ${maintenanceArray[0]}")

                    for(i in 1 until maintenanceArray.size)
                    {
                        Text(text = "- ${maintenanceArray[i]}", lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                        println(maintenanceArray[i])
                    }

                    if(!localStateManager.maintenanceUpdateInd)
                    {
                        Text(
                            text = "Available maintenance slots: ${5 - orderState.maintenanceInstance}",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
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
                            onClick = {
                                if(localStateManager.maintenanceUpdateInd)
                                {
                                    localStateManager = localStateManager.copy(maintenanceUpdateInd = false)

                                    guitarViewModel.updateOrderState(17, false)

                                    guitarViewModel.updateOrderState(18, false)
                                }

                                dialogDismiss()
                                      },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .width(90.dp).height(35.dp)
                        )
                        {
                            Text("Confirm",
                                fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }
                }
            )
        }

        if(orderState.maintenanceFail)
        {
            AlertDialog(
                onDismissRequest = { dialogDismiss(true) },
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
                            onClick = { dialogDismiss(true) },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .height(35.dp).width(90.dp)
                        )
                        {
                            Text("Confirm",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }
                }
            )
        }

        if(localStateManager.maintenanceDeleteConfirm)
        {
            AlertDialog(
                onDismissRequest = { localStateManager = localStateManager.copy(maintenanceDeleteConfirm = false) },
                title = {Text("Confirm delete?", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                text = {Column(verticalArrangement = Arrangement.Top)
                {
                    Text(
                        text = "Data will be unrecoverable after deletion...",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
                },
                confirmButton = {
                    Box(
                        Modifier.background(Color(245,66,87), RoundedCornerShape(10.dp)).height(55.dp).width(120.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center)
                    {
                        TextButton(
                            onClick = { guitarViewModel.orderDelete(localDateIndicator, specificMaintenanceDoc, true) },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .height(35.dp).width(90.dp)
                        )
                        {
                            Text("Confirm",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }
                }
            )
        }

        if(orderState.maintenanceDelete)
        {
            AlertDialog(
                onDismissRequest = {
                    localStateManager = localStateManager.copy(maintenanceDeleteConfirm = false)
                    guitarViewModel.updateOrderState(19, false)
                                   },
                title = {Text("Order deleted successfully!", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
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
                            onClick = {
                                localStateManager = localStateManager.copy(maintenanceDeleteConfirm = false)
                                guitarViewModel.updateOrderState(19, false)
                                      },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .height(35.dp).width(90.dp)
                        )
                        {
                            Text("Confirm",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }
                }
            )
        }
    }
}
