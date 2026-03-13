package com.example.tomkotlinsecondapp

import android.app.AlertDialog
import android.graphics.drawable.Icon
import android.media.tv.AdRequest
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.computeHorizontalBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.tomkotlinsecondapp.ui.theme.TomKotlinSecondAppTheme
//import com.example.tomkotlinsecondapp.Guitar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomKotlinSecondAppTheme {
                    //Greeting(name = "Tomas Escobar Ruiz")

                Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 70.dp, bottom = 70.dp),
                    verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally)
                {
                    GuitarViewPort()
                }

                    //GuitarViewPort()

                    /*ButtonExample1()
                    DialogButton()
                    InputTextField(spacerInd.value)
                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Spacer(modifier = Modifier.height(200.dp))

                       */
            }
        }
    }
}


val colorInput = mutableStateOf(value = "White")
val modelIndVal = mutableStateOf(value = "Telecaster")
val colorIndVal = mutableStateOf(value = false)
val spacerInd = mutableStateOf(value = true)
val cameraInd = mutableIntStateOf(value = 0)

@Composable
fun Greeting (name: String) {

    val imageState = remember { mutableStateOf(value = false) }

    fun clickedImage() {
        imageState.value = true
    }

    if (imageState.value) {
        AlertDialog(
            onDismissRequest = { !imageState.value },
            text = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.endeavour_final_front),
                        contentDescription = "", modifier = Modifier.rotate(degrees = 270.toFloat())
                            .clip(RoundedCornerShape(36.dp)).width(800.dp)
                            .border(width = 2.dp, color = Color.Black, RoundedCornerShape(36.dp))
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { imageState.value = false },
                    modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                        .background(Color.Blue, RoundedCornerShape(16.dp))
                )
                {
                    Text(
                        text = "Confirm",
                        color = Color(0xffffffff),
                        fontFamily = FontFamily.Monospace
                    )
                }
            },

            modifier = Modifier.background(color = Color.LightGray, RoundedCornerShape(36.dp))
                .height(900.dp),

            containerColor = Color.Transparent
            /*content = {
            Image(painter = painterResource(id = R.drawable.endeavour_final_front),
                contentDescription = "", modifier = Modifier.fillMaxSize())
        }*/
            //dismissButton = { Button(onClick = {openingDialog.value = false}) { Text(text = "Dismiss", color = Color(0xffffffff))} }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(
            text = "Hello, my name is $name!",
        )
        Text(
            text = "And this is my first Android app!",
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
        )

        Box(
            modifier = Modifier
                .width(250.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(8.dp))
                .padding(10.dp)
        )
        {
            Text(
                text = "This is the guitar that I'm currently building, it's called Endeavour.",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {
            TextButton(
                onClick = { clickedImage() },
                modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                    .height(80.dp).background(Color.Blue, RoundedCornerShape(16.dp))
            )
            {
                Text(
                    text = "View Guitar Picture.",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(400.dp))

        //SpacerButton()
    }
}

@Composable
fun ButtonExample1 ()
{
    val myContext = LocalContext.current

    val toastDialogInd = remember {mutableStateOf(value = false)}

    var buttonPressCount = 0

    val enableButton = remember {mutableStateOf(value = true)}

    fun clickedButton()
    {
        Toast.makeText(myContext, "Hello, this is a Toast, hehe", Toast.LENGTH_SHORT).show()

        buttonPressCount++

        if(buttonPressCount == 3)
        {
            toastDialogInd.value = true

            enableButton.value = false
        }
    }

    if(toastDialogInd.value)
    {
        AlertDialog (
            onDismissRequest = { !toastDialogInd.value },
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            title = {
                Text (
                    text = "This is the three time toast dialog.",
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text (
                    text = "It means that you have abused the toast button." +
                            "If you're Chris, stop being annoying.",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                TextButton (
                    onClick = { toastDialogInd.value = false },
                    modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                        .background(Color.Blue, RoundedCornerShape(16.dp))
                )
                {
                    Text (
                        text = "Confirm",
                        color = Color(0xffffffff),
                        fontFamily = FontFamily.Monospace
                    )
                }
            },
            //dismissButton = { Button(onClick = {openingDialog.value = false}) { Text(text = "Dismiss", color = Color(0xffffffff))} }
        )
    }



    Column (modifier = Modifier.fillMaxWidth()
        .padding(top = 570.dp), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(70.dp))

        TextButton (onClick = {clickedButton()}, enabled = enableButton.value,  modifier = Modifier.background(Color.Blue, RoundedCornerShape(16.dp))
            .border(BorderStroke(4.dp, Color.Black), RoundedCornerShape(16.dp)))
        {
            Text (text = "Toast Button", color = Color(0xffffffff), fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(70.dp))
    }

}

@Composable
fun DialogButton ()
{
    val openingDialog = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(70.dp))

    Column (modifier = Modifier.fillMaxWidth()
        .padding(top = 720.dp), horizontalAlignment = Alignment.CenterHorizontally)
    {
        TextButton (onClick = { openingDialog.value = true }, modifier = Modifier.border(BorderStroke(4.dp, Color.Black),
        RoundedCornerShape(16.dp)).background(Color.Blue, RoundedCornerShape(16.dp)))
        {
            Text (text = "Dialog Button", color = Color(0xffffffff), fontFamily = FontFamily.Monospace)
        }
    }

    if(openingDialog.value) {
        AlertDialog (
            onDismissRequest = { !openingDialog.value },
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            title = {
                Text (
                    text = "This is the title of the dialog",
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Text (
                    text = "This is the inner text of the dialog window, yaaaaay!",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                InputTextField(spacerInd.value)
            },
            confirmButton = {
                TextButton (
                    onClick = { openingDialog.value = false },
                    modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                        .background(Color.Blue, RoundedCornerShape(16.dp))
                )
                {
                    Text (
                        text = "Confirm",
                        color = Color(0xffffffff),
                        fontFamily = FontFamily.Monospace
                    )
                }
            },
            //dismissButton = { Button(onClick = {openingDialog.value = false}) { Text(text = "Dismiss", color = Color(0xffffffff))} }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TomKotlinSecondAppTheme {
        //Greeting("Tomas Escobar Ruiz")

        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 70.dp, bottom = 70.dp),
            verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally)
        {
            GuitarViewPort()
        }

        //GuitarViewPort()
        ButtonExample1()
        DialogButton()
        InputTextField(spacerInd.value)
    }
}