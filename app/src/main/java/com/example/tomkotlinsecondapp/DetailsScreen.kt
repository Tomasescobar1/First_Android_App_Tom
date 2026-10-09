package com.example.tomkotlinsecondapp

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.contextmenu.modifier.filterTextContextMenuComponents
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable fun DetailsScreen(onNavigateToMenu: () -> Unit, guitarViewModel: GuitarOrder)
{
    val context = LocalContext.current

    var backToggle by remember { mutableStateOf(false) }

    var loginToggle by remember {mutableStateOf(false)}

    var colorOffset by remember { mutableStateOf(Color(66, 203, 245)) }

    val authLoadingState by guitarViewModel.authLoadingState.collectAsStateWithLifecycle()

    val authState by guitarViewModel.authState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top = 200.dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Box(modifier = Modifier.width(280.dp).height(190.dp).background(Color(66, 203, 245), RoundedCornerShape(16.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center)
        {
            Box(
                modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)).width(220.dp)
                    .height(150.dp),
                contentAlignment = Alignment.Center
            )
            {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(
                        text = "Hello and welcome!",
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    Box(modifier = Modifier.width(200.dp).height(20.dp))

                    Text(
                        text = "Please log in to start...",
                        color = Color.Black,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        Box(modifier = Modifier.width(200.dp).height(200.dp))

        Box(
            modifier = Modifier.width(200.dp).height(80.dp)
                .background(Color(66, 203, 245), RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        )
        {
            TextButton(
                onClick = { loginToggle = true },
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    .width(150.dp)
            ) {
                Text(
                    text = "Log in",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if(backToggle)
    {
        AlertDialog(
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
            onDismissRequest = { backToggle = false },
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
                            onClick = { backToggle = false },
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

    LaunchedEffect(authState)
    {
        if(authState)
        {
            loginToggle = false

            guitarViewModel.checkSlotAvailability(true)

            delay(200.milliseconds)

            onNavigateToMenu()

            //guitarViewModel.checkSlotAvailability(true)
        }
    }

    if(loginToggle)
    {
        AlertDialog(
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
            onDismissRequest = { loginToggle = false },
            title = {Text("You are about to sign in with Google...", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
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
                        if(authLoadingState)
                        {
                            Box(modifier = Modifier.height(65.dp).width(225.dp).background(Color.White, RoundedCornerShape(10.dp)),
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
                                modifier = Modifier.background(
                                    Color.White,
                                    RoundedCornerShape(10.dp)
                                )
                                    .width(225.dp).height(65.dp)
                            )
                            {
                                Text(
                                    "Yes, sign in with Google",
                                    fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.height(30.dp).width(80.dp))

                    Box(
                        Modifier.background(Color(245, 66, 87), RoundedCornerShape(15.dp)).width(245.dp).height(85.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                    )
                    {
                        TextButton(
                            onClick = { (context as? Activity)?.finish() },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .width(225.dp).height(65.dp)
                        )
                        {
                            Text("No, leave the app",
                                fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }

                }
            },
            confirmButton = {}
        )
    }

    BackHandler()
    {
        backToggle = true
    }

}
