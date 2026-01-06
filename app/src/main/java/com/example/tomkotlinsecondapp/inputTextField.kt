package com.example.tomkotlinsecondapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InputTextField(inout1:Boolean)
{

    val guitarInstance = Guitar()

    var inputText by remember { mutableStateOf("") }

    var buttonText by remember {mutableStateOf("") }

    var inputCount by remember { mutableIntStateOf(0) }

    fun buttonUpdate(input: String, input2: Int)
    {
        when(input2)
        {
            1 -> {
                guitarInstance.model = input

                buttonText = guitarInstance.model.uppercase()
            }

            2 -> {
                guitarInstance.scaleLength = input.toDouble()

                buttonText = guitarInstance.scaleLength.toString()
            }

            3 -> {
                guitarInstance.color = input

                buttonText = guitarInstance.color.uppercase()
            }

            4 -> {
                guitarInstance.numberOfStrings = input.toInt()

                buttonText = guitarInstance.numberOfStrings.toString()
            }
        }

        //buttonText = guitarInstance.model.uppercase()
    }

    //Spacer(modifier = Modifier.height(200.dp))

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        if (inout1)
        {
            Spacer(modifier = Modifier.height(400.dp))
        }
        else
        {
            Spacer(modifier = Modifier.height(100.dp))
        }

        Box(contentAlignment = Alignment.Center /*, modifier = Modifier.padding(top = 420.dp)*/)
        {
            OutlinedTextField(
                value = inputText,
                onValueChange = { newText -> inputText = newText },
                label = { Text(text = "Label", color = Color.Black) },
                placeholder = { Text(text = "Type in a guitar name: ") },
                //textColor = Color.Black,
                //singleLine = true,
                modifier = Modifier.height(60.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.background(color = Color.Blue, shape = RoundedCornerShape(16.dp)).height(50.dp).width(300.dp)
                .border(width = 4.dp, shape = RoundedCornerShape(16.dp), color = Color.Black).padding(4.dp),
            contentAlignment = Alignment.Center
        )
        {
            Text(text = "Hello $buttonText", color = Color.White, fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = {
            if(inputCount < 4)
            {
                inputCount++

                buttonUpdate(inputText, inputCount)
            }

            else
            {
                buttonText = "Done!"
            }
        },

            modifier = Modifier.background(color = Color.Blue, shape = RoundedCornerShape(16.dp))
                .border(width = 4.dp, color = Color.Black, shape = RoundedCornerShape(16.dp)))
        {
            Text(text = "Update Text", color = Color.White, fontFamily = FontFamily.Monospace)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}