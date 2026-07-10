package com.example.tomkotlinsecondapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable fun MenuScreen(onNavigateToMain: () -> Unit)
{

    var isDeployed by remember {mutableStateOf(false)}

    var colorOffset by remember {mutableStateOf(Color(66, 203, 240))}

    var buttonSizeOffset by remember { mutableDoubleStateOf(0.0) }

    colorOffset = Color(66, 203, 240)

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top = 150.dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
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
                    modifier = Modifier.width((130.0 + buttonSizeOffset).dp).height(130.dp)
                        .border(4.dp, Color.Black)
                        .background(Color(66, 203, 240))
                        .padding(top = 10.dp, bottom = 10.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Button(
                        onClick = { isDeployed = !isDeployed },
                        modifier = Modifier.zIndex(2f).width((100.0 + buttonSizeOffset).dp)
                            .height(100.dp)
                            .background(Color.White, RoundedCornerShape(15.dp)),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.White,
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color(66, 203, 240)
                        )
                    )
                    {
                        Icon(
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null
                            ) { isDeployed = !isDeployed },
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }
                }

                if (isDeployed) {
                    buttonSizeOffset = 70.0
                } else {
                    buttonSizeOffset = 0.0
                }

                AnimatedVisibility(
                    visible = isDeployed,
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
                        //ColorDropDown()

                        //DropDownSection()

                        Box(
                            modifier = Modifier.width(200.dp).height(80.dp)
                                .background(Color(66, 203, 240), RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = {},
                                modifier = Modifier.background(
                                    Color.White,
                                    RoundedCornerShape(12.dp)
                                )
                                    .width(150.dp)
                            ) {
                                Text(
                                    text = "Log In For Maintenance.",
                                    color = Color.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.width(200.dp).height(80.dp)
                                .background(Color(66, 203, 240), RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        )
                        {
                            TextButton(
                                onClick = onNavigateToMain,
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
    }
}
