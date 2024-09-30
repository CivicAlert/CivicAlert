package com.example.civicalertoriginal.Screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // State to hold the image capture instance
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    // State to hold the preview view
    val previewView = remember {
        androidx.camera.view.PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    // State to hold the selected image URI
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    // Launcher for image picker intent
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data.toString()
        }
    }

    LaunchedEffect(key1 = hasCameraPermission) {
        if (hasCameraPermission) {
            // Initialize CameraX and set up image capture
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                imageCapture = ImageCapture.Builder().build()

                // Bind preview and image capture use cases
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
            }, ContextCompat.getMainExecutor(context))
        }
    }

    // Surface for camera preview
    Box(modifier = Modifier.fillMaxSize() . padding(100.dp),) {
        if (hasCameraPermission) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                Button(
                    onClick = { takePicture(imageCapture, context) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text("Take Picture")
                }
            }
        } else {
            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.End
            ) {
                Button(onClick = {
                    ActivityCompat.requestPermissions(
                        context as ComponentActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQUEST_CODE
                    )
                }) {
                    Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End) {
                        Text("Request Camera Permission", modifier = Modifier.size(150.dp,50.dp))
                        Text("Upload Image From Device" , modifier = Modifier.clickable { navController.navigate("upload") })
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    imagePickerLauncher.launch(intent)
                }) {
                    Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Request access to file storage" , modifier = Modifier.clickable { navController.navigate("UploadImage") } .size(150.dp, 50.dp))
                    }

                }
                selectedImageUri?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Selected Image URI: $it")
                }
            }
        }
    }
}

private fun takePicture(imageCapture: ImageCapture?, context: Context) {
    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "civic_alert_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture?.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri ?: photoFile.absolutePath
                println("Image saved: $savedUri")
            }

            override fun onError(exception: ImageCaptureException) {
                println("Error capturing image: ${exception.message}")
            }
        }
    )
}

private const val CAMERA_PERMISSION_REQUEST_CODE = 1001

@RequiresApi(Build.VERSION_CODES.O)
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraPreview() {
    //  CameraScreen(navController = NavController)
}
