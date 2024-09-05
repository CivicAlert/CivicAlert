@file:Suppress("UNUSED_EXPRESSION")

package com.example.civicalertoriginal.Components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.civicalertoriginal.R


@Composable
fun LogAndForgotHeader(screenLabel:String) {
    Column ( modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Picture Logo", modifier = Modifier.size(150.dp, 150.dp))
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = screenLabel , modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .wrapContentSize(),
            style = TextStyle( color = Color.Black,
                fontStyle = FontStyle.Normal,
                fontSize = 20.sp)
        )

    }
}

//Text fields that accept text only
@Composable
fun TextFields(value:String,onChange:(String)->Unit,fieldLabel:String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}

//Text fields that accept Numbers only
@Composable
fun NumberTextFields(value:String,onChange:(String)->Unit,fieldLabel:String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}

//Text fields that accept email only
@Composable
fun EmailTextFields(value:String,onChange:(String)->Unit,fieldLabel:String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}

@Composable
fun PasswordTextFields(value:String,onChange:(String)->Unit,fieldLabel:String){

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if(passwordVisibility )
        painterResource(id = R.drawable.eye)
    else
        painterResource(id = R.drawable.hidden)

    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value ,
            onValueChange = onChange,
            supportingText = {
                Text(text = "Passwords must match")},
            placeholder = { Text(text = fieldLabel, color = Color.Green)},


            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(painter = icon, contentDescription ="",
                        modifier = Modifier.size(20.dp,20.dp))

                }

            }, visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}


@Composable
fun BottomButtons(name: String, onClick: () -> Unit,){
    Button(onClick = onClick, shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(Color.Green),
        modifier = Modifier
            .width(200.dp)) {
        Text(text = name, modifier = Modifier
            .size(80.dp, 30.dp)
            .padding(start = 17.dp, top = 4.dp)
            .align(Alignment.CenterVertically),
            color = Color.Black)
    }
}
@Composable
fun SubmitButton(name: String, onClick: () -> Unit,){
    Button(onClick = onClick, shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(Color.Green),
        modifier = Modifier
            .width(260.dp)
            .padding(start = 100.dp)) {
        Text(text = name, modifier = Modifier
            .size(80.dp, 30.dp)
            .padding(start = 17.dp, top = 4.dp)
            .align(Alignment.CenterVertically),
            color = Color.Black)
    }
}
@Composable
fun Logo(){
    Image(painter = painterResource(id = R.drawable.logo), contentDescription ="" )
}
@Composable
fun CardButton(iconRes: Int, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(160.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White), // Set the background color to white
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Set elevation to add shadow
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "",
                modifier = Modifier.size(40.dp)
            )
            Text(text = label)
        }
    }
}

@Composable
fun LogBottomButtons(name: String, onClick: () -> Unit, enabled: Boolean){
    Button(onClick = onClick, enabled = enabled, shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(Color.Green),
        modifier = Modifier
            .width(200.dp)) {
        Text(text = name, modifier = Modifier
            .size(80.dp, 30.dp)
            .padding(start = 17.dp, top = 4.dp)
            .align(Alignment.CenterVertically),
            color = Color.Black)
    }
}
@Composable
fun SignUpText(value: String) {
    var checkedState by remember { mutableStateOf(false) } // State for checkbox

    Row(modifier = Modifier.padding(2.dp)) {
        Text(text = value, modifier = Modifier.padding(end = 8.dp)) // Added padding to the text

        Checkbox(
            checked = checkedState, // Use the state for the checkbox
            onCheckedChange = { checkedState = it }, // Update the state when clicked
            enabled = true,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 16.dp, start = 12.dp)
                .clip(RoundedCornerShape(50.dp))
        )
    }
}

@Composable
fun InstructionText(value: String){
    Text(
        text = value,
        style = TextStyle(
            fontStyle = FontStyle.Normal,
            fontSize = 15.sp,
            color = Color.Black
        )
    )
}
@Composable
fun LocationTextFields(value: String, onChange: (String) -> Unit, fieldLabel: String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green)},
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(35.dp, 35.dp)
                        .clickable { },
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon"
                ) },
            keyboardOptions = KeyboardOptions.Default,
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}
@Composable
fun ReportDescriptionText(value1: String, value:String,){
    Column {

        Text(text = value1, style = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 20.sp, fontWeight = FontWeight.Bold,
            fontSynthesis = FontSynthesis.All
        )
        )
        Text(text = value, style = TextStyle(
            fontFamily = FontFamily.Default,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
        )
        )
    }
}
@Composable
fun PictureTextFields(value: String, onChange: (String) -> Unit, navController: NavController){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
           // placeholder = { Text(text = fieldLabel, color = Color.Green)},
            trailingIcon = {
                Image(painter = painterResource(id = R.drawable.camera), contentDescription ="" ,
                    modifier = Modifier.clickable { navController.navigate("Camera")}
                        .size(25.dp)) },
            keyboardOptions = KeyboardOptions.Default,
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}
@Composable
fun DescriptionTextFields(value: String, onChange: (String) -> Unit, fieldLabel: String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
           placeholder = { Text(text = fieldLabel, color = Color.Green)},
            keyboardOptions = KeyboardOptions.Default,
            textStyle = TextStyle(color = Color.Black ), modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(
    selectedIncident: String,
    onIncidentSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val Incidents= arrayOf("Water", "Electricity", "Pothole", "Other")
    var expanded by remember { mutableStateOf(false) }
   // var selectedText by remember { mutableStateOf(Incidents[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        androidx.compose.material3.ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedIncident,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Incidents.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                           onIncidentSelected(item)
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun SignUpBottomButtons( name:String , onClick : ()-> Unit ){
    Button(
        onClick = onClick,
        shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(Color.Green),
        modifier = Modifier
            .width(200.dp)
    ) {
        Text(text = name, modifier = Modifier
            .size(80.dp, 30.dp)
            .padding(start = 17.dp, top = 4.dp)
            .align(Alignment.CenterVertically),
            color = Color.Black)
    }
}
@Composable
fun ProfileText(description: String , value: String, onSave:(String)-> Unit) {
    var textFieldVisible by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(value) }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        // Clickable Text
        Text(text = description, fontFamily = FontFamily.SansSerif, fontSize = 17.sp)
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = value,
            modifier = Modifier.clickable {
                textFieldVisible = true
            },
            fontSize = 25.sp,
            fontFamily = FontFamily.Default,
            style = MaterialTheme.typography.bodyMedium.copy(
                textDecoration = TextDecoration.Underline
            )
        )

        // Conditional Text Field
        if (textFieldVisible) {
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    textFieldVisible = false
                    onSave(textFieldValue)
                    // logic to do when saving thee data
                },
                modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.End)
            ) {
                Text("Save")
            }
        }
    }
}
@Composable
fun UpdateProfileButton(name: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(Color.Green),
        modifier = Modifier
            .width(400.dp)
    ) {
        Text(
            text = name, modifier = Modifier
                .size(80.dp, 30.dp)
                .padding(start = 17.dp, top = 4.dp)
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            color = Color.Black
        )
    }
}

@Composable
fun BottomButtonsMyProfile(name: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, shape = ButtonDefaults.shape,
        colors = ButtonDefaults.buttonColors(Color.Red),
        modifier = Modifier
            .width(400.dp)
    ) {
        Text(
            text = name, modifier = Modifier
                .size(80.dp, 30.dp)
                .padding(start = 17.dp, top = 4.dp)
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            color = Color.Black
        )
    }
}




@Composable
fun ContactUsContactButton(value: String, phoneNumber: String) {
    val context = LocalContext.current

    Button(
        onClick = {
            // Dialer Intent
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$08299999999")
            }
            context.startActivity(dialIntent)
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black, containerColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 16.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(170.dp, 50.dp)
    ) {
        Icon(imageVector = Icons.Default.Call, contentDescription = "", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = value, fontSize = 15.sp)
    }
}

@Composable
fun ContactUSEmailButton(value: String, email: String) {
    val context = LocalContext.current

    Button(
        onClick = {
            // Email Intent
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:Civicalert300@gmail.com") // Only email apps should handle this
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email)) // Recipient
                putExtra(Intent.EXTRA_SUBJECT, "Your Subject Here") // Optional subject
                putExtra(Intent.EXTRA_TEXT, "Your message here.") // Optional message body
            }

            // Verify there is an email app installed before trying to open it
            if (emailIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(emailIntent)
            } else {
                Toast.makeText(context, "No email app found.", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black, containerColor = Color.White
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 16.dp),
        modifier = Modifier
            .padding(8.dp)
            .size(170.dp, 50.dp)
    ) {
        Icon(imageVector = Icons.Default.Email, contentDescription = "", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.size(5.dp))
        Text(text = value, fontSize = 18.sp)
    }
}

@Composable
fun ContactUsWhatsApp(value: String) {
    val uriHandler = LocalUriHandler.current

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(x = -20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = "",
                modifier = Modifier.size(35.dp)
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "082222222222",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://wa.me/082222222222")
                    }
                )
            }
        }
    }
}
@Composable
fun ContactUsWMessanger(value: String) {
    val uriHandler = LocalUriHandler.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.messenger),
                contentDescription = "",
                modifier = Modifier.size(35.dp)
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Facebook",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://www.messenger.com/t/facebook")
                    }
                )
            }
        }
    }
}
@Composable
fun ContactUsInsta(value: String) {
    val uriHandler = LocalUriHandler.current

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(x = -25.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.instagram),
                contentDescription = "",
                modifier = Modifier.size(35.dp)
            )
            Column(modifier = Modifier.padding(start = 10.dp)){
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "instagram",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://www.instagram.com/")
                    }
                )
            }
        }
    }
}
@Composable
fun ContactUsTwitter(value: String) {
    val uriHandler = LocalUriHandler.current

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(x = -14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.twitter),
                contentDescription = "",
                modifier = Modifier.size(35.dp)
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "twitter",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://twitter.com/")
                    }
                )
            }
        }
    }
}
