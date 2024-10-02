package com.example.civicalertoriginal.Screens
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
fun Camera(navController: NavController) {
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
   // var selectedImageUri by remember { mutableStateOf<String?>(null) }

    // Launcher for image picker intent
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            selectedImageUri = result.data?.data.toString()
//        }
//    }

    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
    isGranted -> hasCameraPermission = isGranted
    }


    if (!hasCameraPermission) {
        AccessCameraScreen(permissionLauncher)
    }
        else {
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
    }

    // Surface for camera preview
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(100.dp),) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp)) {
            Button(
                onClick = { takePicture(imageCapture, context) },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text("Take Picture")
            }
        }
    }
}
@Composable
fun AccessCameraScreen(permissionLauncher: ManagedActivityResultLauncher<String, Boolean>) {
    // Screen displayed before permission is granted
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Access Camera")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                permissionLauncher
            }) {
                Text(text = "Request Camera Access")
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
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile).toString()
                println("Image saved: $savedUri")
                //onImageCapture(savedUri.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                println("Error capturing image: ${exception.message}")
            }
        }
    )
}

private const val CAMERA_PERMISSION_REQUEST_CODE = 1001

@Composable
fun UploadImageScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    // Launcher to select an image from the gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
            uri?.let {
                // Load the image from the URI
                val inputStream = context.contentResolver.openInputStream(it)
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
        }
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Button to trigger the image picker
        Button(onClick = {
            // Check if permission is required (for Android 13+)
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != Activity.RESULT_OK
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else {
                imagePickerLauncher.launch("image/*")
            }
        }) {
            Text(text = "Select Image")
        }
      Button(onClick = { navController.navigate("updatedCamera") }) {
          Text(text = "Access Camera")
      }
        

        // Display the selected image
        selectedImageUri?.let {
            Spacer(modifier = Modifier.height(16.dp))
            bitmap?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                )
            }
        }
    }
}

