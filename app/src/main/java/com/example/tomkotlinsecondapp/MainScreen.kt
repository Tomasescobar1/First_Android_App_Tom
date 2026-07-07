package com.example.tomkotlinsecondapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable
import kotlin.system.exitProcess

fun Context.triggerAppReboot()
{
    val packageManager = this.packageManager

    val launchIntent = packageManager.getLaunchIntentForPackage(this.packageName)

    if(launchIntent != null)
    {
        val restartIntent = Intent.makeRestartActivityTask(launchIntent.component)

        this.startActivity(restartIntent)

        exitProcess(0)
    }
}

@Composable fun MainScreen(guitarViewModel: GuitarOrder = viewModel())
{
    val context = LocalContext.current

    var restartToggle by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Transparent).zIndex(2f)
            .clickable(interactionSource = null, indication = null){ guitarViewModel.updateDataState(7, " ", 0.0) },
        contentAlignment = Alignment.Center
    )
    {
        GuitarViewPort()
    }

    BackHandler {
        restartToggle = true
    }

    if(restartToggle)
    {
        AlertDialog(
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
            onDismissRequest = {},
            title = {Text("You are about to leave for the main menu...", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)},
            text = {
                Column( modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally )
                {
                    Text(
                        text = "This will reboot the app, are your sure?", overflow = TextOverflow.Clip,
                        lineHeight = 30.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)

                    Box(
                        Modifier.padding(top = 10.dp).background(Color(66, 203, 245), RoundedCornerShape(15.dp)).width(245.dp).height(85.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center
                    )
                    {
                        TextButton(
                            onClick = { guitarViewModel.updateDataState(5, "", 0.0, true) },
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
                            onClick = { context.triggerAppReboot() },
                            modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp))
                                .width(225.dp).height(65.dp)
                        )
                        {
                            Text("Yes, go to the main menu",
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