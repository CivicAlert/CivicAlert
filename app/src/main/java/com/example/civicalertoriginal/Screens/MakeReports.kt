package civicalertoriginal.Screen

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
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.Components.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class Reports(
    val incidentType: String = "",
    val location: String = "",
    val description: String ="",
    val dateTime: String ="",

)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeReports(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }
    //var isVisible by remember { mutableStateOf(false) }
    var picture by remember { mutableStateOf("") }

    val savedImageUri = navController.currentBackStackEntry
        ?.savedStateHandle?.getLiveData<String>("imageUri")

    savedImageUri?.observe(LocalLifecycleOwner.current) { uri ->
        picture = uri // Set the image URI in PictureTextField
    }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Surface(color = Color.White) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000, easing = LinearEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(1000, easing = LinearEasing)
            )
        ) {
            AnimatedMakeReports(navController){isVisible = false
            navController.navigate("Dashboard")}
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimatedMakeReports(navController: NavController, onClose:()->Unit) {
    val database = Firebase.database
    val myRef = database.getReference("Make Report Instance")
    val auth = FirebaseAuth.getInstance();
    var location by remember { mutableStateOf("Enter Location") }
    var description by remember { mutableStateOf("Brief details of the incident") }
    var picture by remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentDateTime = LocalDateTime.now()
    val formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row ( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {

            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", modifier = Modifier
                .size(30.dp)
                .clickable { onClose() },
                tint = Color.Red)
            Spacer(modifier = Modifier.size(25.dp))
            Text(
                text = "Make A Report",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        ReportDescriptionText(
            value1 = "Incident",
            value = "Choose Incident type"
        )
        var selectedIncident by remember { mutableStateOf("Water") }
        ExposedDropdownMenuBox( selectedIncident = selectedIncident,
            onIncidentSelected = {newIncident -> selectedIncident = newIncident})

        ReportDescriptionText(
            value1 = "Location(Optional)",
            value = "Share the location of the incident"
        )
        LocationTextFields(value = location, onChange = { location = it }, fieldLabel = " Enter location" )

        ReportDescriptionText(
            value1 = "Photos*",
            value = "Take photos of the incident you are reporting"
        )
        PictureTextFields(value = picture, onChange = { picture = it },  navController = navController)
       // UploadImageScreen(navController = navController, onImageSelected = {imageUrl -> picture = imageUrl})

        ReportDescriptionText(
            value1 = "Report Description*",
            value = "Short Description of the incident"
        )
        DescriptionTextFields(value = description, onChange = { description = it }, fieldLabel = "describe the incident" )

        val userReport = Reports(
            incidentType = selectedIncident,
            location = location,
            description = description,
            dateTime =  formattedDateTime
           )

        fun saveReport(report:Reports) {
            val userId = myRef.push().key ?: return
            myRef.child(userId).setValue(report).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Handle success
                    println("Report saved")
                    Toast.makeText(
                        context,
                        "Your report has been submitted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Handle failure
                    task.exception?.let {
                        println("Error saving user: ${it.message}")
                    }
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally) {
            SubmitButton(name = "Submit") {
                saveReport(userReport)
                navController.navigate("Dashboard")}
        }
        Spacer(modifier = Modifier.size(8.dp))

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MakeReportsPreview() {
    val navController = rememberNavController()
    MakeReports(navController = navController)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraReport(navController: NavController) {
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
                    val preview = androidx.camera.core.Preview.Builder().build().also {
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
                onClick = {
                    takePicture(imageCapture,navController,context,){imageUri ->
                        navController.previousBackStackEntry?.savedStateHandle?.set("imageUri", imageUri)
                    }
                          },
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

private fun takePicture(imageCapture: ImageCapture?,navController: NavController, context: Context, onImageCaptured:(String)-> Unit) {
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
                navController.previousBackStackEntry?.savedStateHandle?.set("imageUri", savedUri)
                navController.popBackStack( )// Pass the URI to the PictureTextField
            }

            override fun onError(exception: ImageCaptureException) {
                println("Error capturing image: ${exception.message}")
            }
        }
    )
}

private const val CAMERA_PERMISSION_REQUEST_CODE = 1001

@Composable
fun UploadImageReport(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    // Launcher to select an image from the gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
            uri?.let {
               val imageUri = uri.toString()
                navController.previousBackStackEntry?.savedStateHandle?.set("imageUri",imageUri)
                navController.popBackStack()
                val inputStream = context.contentResolver.openInputStream(it)
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
               // onImageSelected(uri.toString())
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
        Button(onClick = { navController.navigate("Camera") }) {
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





