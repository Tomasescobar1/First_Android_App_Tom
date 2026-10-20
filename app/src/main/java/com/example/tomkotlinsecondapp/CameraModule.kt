package com.example.tomkotlinsecondapp

import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview()
{
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    LaunchedEffect(lifecycleOwner)
    {
        //val cameraProvider = ProcessCameraProvider.getInstance(context).await()

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider {
                request -> surfaceRequest = request
            }
        }


    }
}