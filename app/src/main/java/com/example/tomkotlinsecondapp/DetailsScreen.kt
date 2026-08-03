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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable fun DetailsScreen(onNavigateToMenu: () -> Unit)
{
    val context = LocalContext.current

    var backToggle by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top = 200.dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Box(modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)).width(200.dp).height(50.dp))
        {
            Text(
                text = "Hello, this is the details screen.",
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight= FontWeight.Bold
            )
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
                onClick = onNavigateToMenu,
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    .width(150.dp)
            ) {
                Text(
                    text = "Go to Menu",
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

    BackHandler()
    {
        backToggle = true
    }

}
