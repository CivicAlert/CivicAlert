package com.example.civicalertoriginal.Screens

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import java.lang.reflect.Modifier
import java.nio.file.WatchEvent

@Composable
fun UploadImage(navController: NavController) {

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current


   val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
       uri: Uri? -> selectedImageUri = uri
   }
    Column (modifier = androidx.compose.ui.Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom){
        Button(onClick = {val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE) }
        val resolveInfo = context.packageManager.resolveActivity(intent, 0)
            if (resolveInfo!= null){
                imagePickerLauncher.launch("image/*")
            }else{
                Toast.makeText(context,"the application cannot select the message", Toast.LENGTH_SHORT).show()
            }



































































        }) {
           Column( horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Bottom) {
                Text(text = "Upload Image")
            }
        }
        Spacer(modifier = androidx.compose.ui.Modifier.size(16.dp))

        selectedImageUri?.let { uri ->  (Image(painter = rememberImagePainter(data = uri), contentDescription = "Selected image",
            modifier = androidx.compose.ui.Modifier.height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop)) }

    }
}
