package com.example.civicalertoriginal.Components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
@Composable
fun TextFields(value:String,onChange:(String)->Unit,fieldLabel:String){
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
@Composable
fun BottomButtons( name:String , onClick : ()-> Unit ){
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
fun SignUpText(value: String){
    Row ( modifier = Modifier.padding(10.dp)){
        var state by remember { mutableStateOf("") }
        Text(text = value, modifier = Modifier
        )

        Checkbox(checked = false, onCheckedChange = {}, enabled = true, modifier = Modifier
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
fun ReportDescriptionText(value1: String, value:String){
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
fun PictureTextFields(value: String, onChange: (String) -> Unit, fieldLabel: String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green)},
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(35.dp, 35.dp)
                        .clickable { },
                    imageVector = Icons.Default.Person,
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
fun DescriptionTextFields(value: String, onChange: (String) -> Unit, fieldLabel: String){
    Column (verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = value , onValueChange = onChange,
            placeholder = { Text(text = fieldLabel, color = Color.Green)},
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(35.dp, 35.dp)
                        .clickable { },
                    imageVector = Icons.Default.Info,
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox() {
    val context = LocalContext.current
    val Incidents= arrayOf("Water", "Electricity", "Pothole", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(Incidents[0]) }

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
                value = selectedText,
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
                            selectedText = item
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



