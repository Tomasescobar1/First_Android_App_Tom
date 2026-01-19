package com.example.tomkotlinsecondapp

import android.icu.text.Transliterator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.filament.Skybox
import io.github.sceneview.Scene
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

//import androidx.compose.material3

@Composable
fun GuitarViewPort()
{

    val engine = rememberEngine()

    val modelLoaderDef = rememberModelLoader(engine)

    val materialLoaderDef = rememberMaterialLoader(engine)

    val environmentLoaderDef = rememberEnvironmentLoader(engine)

    val environment = remember {
        environmentLoaderDef.createHDREnvironment("environments/sky_2k.hdr")
    }

    val view = rememberView(engine)

    Box(modifier = Modifier.border(4.dp, Color.Black, RoundedCornerShape(24.dp))
        .height(800.dp).width(400.dp).background(Color.White), contentAlignment = Alignment.Center)
    {

        Scene(
            modifier = Modifier.fillMaxSize().border(14.dp, Color.White, RoundedCornerShape(24.dp)),

            engine = engine,

            view = view,
            renderer = rememberRenderer(engine),
            scene = rememberScene(engine),

            modelLoader = modelLoaderDef,

            childNodes = rememberNodes {
                val modelNode = ModelNode(
                    modelInstance = modelLoaderDef.createModelInstance("growler_body.glb"),
                )

                modelNode.scale = Scale(0.8f)

                modelNode.rotation = Rotation(0.0f, 0.0f, 90.0f)

                add(modelNode)
            },

            cameraNode = rememberCameraNode(engine)
            {
                position = Position(0.0f, 0.0f, 1.0f)
            },

            cameraManipulator = rememberCameraManipulator(),

            mainLightNode = rememberMainLightNode(engine){
                intensity = 100_000.0f
            }
        )
        /*Text(
            text = "This is going to be the guitar viewport.",
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 20.sp,
            modifier = Modifier.padding(15.dp)
        )*/
    }
}
