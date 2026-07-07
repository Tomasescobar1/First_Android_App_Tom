package com.example.tomkotlinsecondapp

import android.graphics.drawable.Icon
import android.icu.text.Transliterator
import android.view.Choreographer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.filament.Skybox
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.Scene
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import com.example.tomkotlinsecondapp.ColorDropDown
import com.google.android.filament.MaterialInstance
import io.github.sceneview.safeDestroyMaterialInstance
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GuitarViewPort(guitarViewModel: GuitarOrder = viewModel())
{

    val cDataState by guitarViewModel.dataState.collectAsStateWithLifecycle()

    val fDataState by guitarViewModel.deployedState.collectAsStateWithLifecycle()

    val engine = rememberEngine()

    val modelLoaderDef = rememberModelLoader(engine)

    val materialLoaderDef = rememberMaterialLoader(engine)

    var isLoading by remember {mutableStateOf(true)}

    var cameraNode1 = rememberCameraNode(engine)

    var cameraOrbitHome = Float3(0.0f, 0.0f, 1.3f)

    var cameraPosition = Float3(0.0f, 0.0f, 1.3f)

    var cameraRotation = Float3(0.0f, 0.0f, 0.0f)

    var cameraNodeDef = rememberCameraNode(engine){position = cameraPosition; rotation = cameraRotation}

    val colorMaterialRed = materialLoaderDef.createColorInstance(
        color = Color.Red,
        roughness = 0.0f,
        metallic = 0.0f,
    )

    val colorMaterialBlue = materialLoaderDef.createColorInstance(
        color = Color(red = 66, green = 212, blue = 245),
        roughness = 0.0f,
        metallic = 0.0f,
    )

    val colorMaterialOGreen = materialLoaderDef.createColorInstance(
        color = Color(red = 37, green = 92, blue = 44),
        roughness = 0.0f,
        metallic = 0.0f,
    )

    val colorMaterialWhite = materialLoaderDef.createColorInstance(
        color = Color.White,
        roughness = -10.7f,
        metallic = 0.0f,
        reflectance = 0.5f,
    )

    val environmentLoaderDef = rememberEnvironmentLoader(engine)

    val view = rememberView(engine)

    var modelNode = ModelNode(
        modelInstance = modelLoaderDef.createModelInstance("growler_25_inch_body.glb"),
    )

    val neckModelNode = ModelNode(
        modelInstance = modelLoaderDef.createModelInstance("25_inch_neck_assembly.glb")
    )

    var componentsNode = ModelNode(
        modelInstance = modelLoaderDef.createModelInstance("growler_25_components.glb")
    )

    Box(modifier = Modifier
        .height(900.dp).width(400.dp).background(Color.White), contentAlignment = Alignment.Center,)
    {

        LaunchedEffect(cDataState.colorInput, cDataState.modelIndVal) {

            when (cDataState.colorInput)
            {
                "Red" -> {
                    modelNode.setMaterialInstance(colorMaterialRed)
                }

                "Sky Blue" -> {
                    modelNode.setMaterialInstance(colorMaterialBlue)
                }

                "Olive Green" -> {
                    modelNode.setMaterialInstance(colorMaterialOGreen)
                }

                "White" -> {
                    modelNode.setMaterialInstance(colorMaterialWhite)
                }
            }
        }

        LaunchedEffect(cDataState.colorInput, cDataState.modelIndVal, cDataState.cameraInd)
        {
            isLoading = true

            delay(1500L.milliseconds)

            isLoading = false
        }

        if(isLoading)
        {
            Box(modifier = Modifier.fillMaxSize().background(Color.LightGray, RoundedCornerShape(22.dp)).zIndex(1f), contentAlignment = Alignment.Center)
            {
                CircularProgressIndicator(modifier = Modifier.size(100.dp), strokeWidth = 10.dp, trackColor = Color(66, 203, 245), color = Color.White)
            }
        }

        if(fDataState.deployedState)
        {
            Box(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Transparent)
                    .zIndex(2f)
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) { guitarViewModel.updateDataState(7, " ", 0.0) },
                contentAlignment = Alignment.Center
            )
            {}
        }

        key(cDataState.colorInput, cDataState.modelIndVal, cDataState.cameraInd)
        {
            Scene(
                modifier = Modifier.fillMaxSize().border(6.dp, Color.White)
                    .align(Alignment.Center),

                engine = engine,

                view = view,
                renderer = rememberRenderer(engine),
                scene = rememberScene(engine),

                materialLoader = materialLoaderDef,

                modelLoader = modelLoaderDef,

                childNodes = rememberNodes {

                    when (cDataState.modelIndVal)
                    {
                        "Telecaster" -> {
                            modelNode = ModelNode(
                                modelInstance = modelLoaderDef.createModelInstance("tele_25_inch_body.glb"),
                            )

                            componentsNode = ModelNode(
                                modelInstance = modelLoaderDef.createModelInstance("tele_25_components.glb")
                            )

                            modelNode.rotation = Rotation(0.0f,0.0f, 90.0f)

                            modelNode.position = Position(0.0f, -0.32f, 0.0f)

                            neckModelNode.rotation = Rotation(0.0f, 0.0f, 90.0f)

                            neckModelNode.position = Position(0.0f,-0.32f, 0.0f)

                            componentsNode.rotation = Rotation(0.0f, 0.0f, 90.0f)

                            componentsNode.position = Position(0.0f, -0.32f, 0.0f)
                        }

                        "Growler" -> {
                            modelNode = ModelNode(
                                modelInstance = modelLoaderDef.createModelInstance("growler_25_inch_body.glb"),
                            )

                            componentsNode = ModelNode(
                                modelInstance = modelLoaderDef.createModelInstance("growler_25_components.glb")
                            )

                            modelNode.rotation = Rotation(0.0f,0.0f, 90.0f)

                            modelNode.position = Position(0.0f, -0.3f, 0.0f)

                            neckModelNode.rotation = Rotation(0.0f, 0.0f, 90.0f)

                            neckModelNode.position = Position(0.0f,-0.3f, 0.0f)

                            componentsNode.rotation = Rotation(0.0f, 0.0f, 90.0f)

                            componentsNode.position = Position(0.0f, -0.3f, 0.0f)
                        }
                    }

                    modelNode.setMaterialInstance(colorMaterialWhite)

                    add(modelNode)

                    add(neckModelNode)

                    add(componentsNode)

                },

                cameraNode = cameraNodeDef

            )

        }

        Column(modifier = Modifier.width(400.dp).height(900.dp).zIndex(1f)
            .border(8.dp, Color.White, RoundedCornerShape(22.dp)), verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start)
        {

        }

        Column(modifier = Modifier.width(400.dp).height(900.dp).zIndex(3f), verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start)
        {
            FABComponent()

            ConfirmSection()
        }
    }

}
