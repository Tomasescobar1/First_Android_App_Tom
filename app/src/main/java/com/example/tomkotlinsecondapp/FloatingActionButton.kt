package com.example.tomkotlinsecondapp

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@Composable
fun FABComponent(guitarViewModel: GuitarOrder) {

    val isDeployed by guitarViewModel.deployedState.collectAsStateWithLifecycle()

    val offlineState by guitarViewModel.isOffline.collectAsStateWithLifecycle()

    val context = LocalContext.current

    //var exitToggle by remember {mutableStateOf(false)}

    var fabOffset by remember {mutableStateOf(Offset(0f, 0f))}

    var colorOffset by remember {mutableStateOf(Color(66, 203, 240))}

    var sizeOffset by remember {mutableIntStateOf(480)}

    colorOffset = if(isDeployed.deployedState)
    {
        Color.Transparent
    }
    else
    {
        Color(66, 203, 240)
    }

    sizeOffset = when {
        offlineState -> 540
        else -> 480
    }

    FloatingActionButton (
        onClick = {},
        modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).zIndex(2f)
            .clickable(interactionSource = null, indication = null){}
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
            modifier = Modifier.clickable(interactionSource = null, indication = null){ guitarViewModel.updateDataState(6, " ", 0.0) },
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.Black
        )

        AnimatedVisibility(visible = isDeployed.deployedState)
        {
            Column(
                modifier = Modifier.zIndex(2f).width(200.dp).height(sizeOffset.dp).clickable(interactionSource = null, indication = null){},
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            )
            {
                if(offlineState)
                {
                    Box(
                        modifier = Modifier.width(200.dp).height(70.dp)
                            .background(Color(245, 66, 87), RoundedCornerShape(16.dp))
                            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Box(
                            modifier = Modifier.width(150.dp).height(40.dp)
                                .background(Color.White, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Text(
                                text = "Offline",
                                color = Color.Black,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                ColorDropDown()

                DropDownSection(guitarViewModel = guitarViewModel)

                Box(
                    modifier = Modifier.width(200.dp).height(80.dp)
                        .background(Color(245, 66, 87), RoundedCornerShape(16.dp))
                        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                )
                {
                    TextButton(
                        onClick = { guitarViewModel.updateDataState(5, "", 0.0) },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                            .width(150.dp)
                    ) {
                        Text(
                            text = "Exit App",
                            color = Color.Black,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if(isDeployed.exitDeploy)
        {
            AlertDialog(
                modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                onDismissRequest = {},
                title = {Text("You are about to leave the app...", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
                text = {
                    Column( modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally )
                    {
                        Text(
                            text = "Are your sure?", overflow = TextOverflow.Clip,
                            lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                        Box(
                            Modifier.padding(top = 10.dp).background(Color(66, 203, 245), RoundedCornerShape(15.dp)).width(245.dp).height(65.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                               onClick = { guitarViewModel.updateDataState(5, "", 0.0, true) },
                                modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                    .width(225.dp).height(45.dp)
                            )
                            {
                                Text("No, continue on the app",
                                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                    color = Color.Black)
                            }
                        }

                        Box(modifier = Modifier.height(30.dp).width(80.dp))

                        Box(
                            Modifier.background(Color(245, 66, 87), RoundedCornerShape(15.dp)).width(245.dp).height(65.dp)
                                .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = { (context as? Activity)?.finish() },
                                modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                    .width(225.dp).height(45.dp)
                            )
                            {
                                Text("Yes, leave the app",
                                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                    color = Color.Black)
                            }
                        }

                    }
                },
                confirmButton = {}
            )
        }
    }
}