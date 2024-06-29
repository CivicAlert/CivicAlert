package com.example.civicalertoriginal.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.civicalertoriginal.R

data class Report(
    val title: String,
    val location: String,
    val date: String,
    val description: String,
    val imageRes: Int
)

enum class FilterOption {
    Recent, // Sorting by recent
    Old // Sorting from oldest to latest
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReports(navController: NavController) {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 0.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Reports",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 4.dp, start = 12.dp)
            )

            var searchText by remember { mutableStateOf("") }
            var filterOption by remember { mutableStateOf(FilterOption.Recent) }

            val reports = remember { generateDummyReports() }
            val filteredReports = when (filterOption) {
                FilterOption.Recent -> reports.sortedByDescending { it.date }
                FilterOption.Old -> reports.sortedBy { it.date }
            }.filter {
                it.title.contains(searchText, ignoreCase = true) || it.location.contains(searchText, ignoreCase = true)
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .fillMaxSize()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(2f)
                                .width(25.dp)
                                .height(60.dp),
                            value = searchText,
                            onValueChange = { searchText = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon"
                                )
                            },
                            placeholder = { Text("Search") },
                            shape = RoundedCornerShape(50),
                            singleLine = true,
                            trailingIcon = {
                                ExposedDropdownMenu(
                                    items = FilterOption.values().toList(),
                                    selectedItem = filterOption,
                                    onItemSelected = { filterOption = it },
                                )
                            }
                        )
                    }
                }
                items(filteredReports) { report ->
                    Column {
                        ExpandableReportItem(report = report)
                        Divider(color = Color.Black, thickness = 2.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun <T> ExposedDropdownMenu(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    label: String = ""

) {
    var expanded by remember { mutableStateOf(false) }


    Box(
    ) {
        val shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 25.dp,
            bottomStart = 0.dp,
            bottomEnd = 25.dp
        )

        OutlinedTextField(
            value = selectedItem.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown icon"
                    )
                }
            },
            shape = shape, // Apply the custom shape
            modifier = Modifier
                .size(150.dp, 64.dp)
                .clip(shape) // Clip content to the shape
                .clipToBounds() // Clip the bounds of the TextField
                .offset(y = -4.dp)
                .fillMaxHeight()
                .padding(end = 4.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.toString()) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun ExpandableReportItem(report: Report) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = report.imageRes),
                    contentDescription = "Report Image",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .width(50.dp)
                ) {
                    Text(
                        text = report.title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.location,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = report.date,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = report.description,
                            modifier = Modifier
                                .height(25.dp)
                                .clipToBounds(),// Show only the first 10 letters
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                }
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Expand icon",
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${report.description}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { expanded = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

fun generateDummyReports(): List<Report> {
    return listOf(
        Report("Pothole in Main Street", "Main Street", "2024-06-27", "There's a large pothole on Main Street that needs to be fixed.", R.drawable.photo),
        Report("Streetlight not working", "Elm Street", "2024-06-26", "The streetlight on Elm Street has been out for a week.", R.drawable.photo),
        Report("Graffiti on wall", "Community Center", "2024-06-25", "Someone spray-painted graffiti on the community center wall.", R.drawable.photo),
        Report("Water leakage", "Pine Avenue", "2024-06-24", "Water leakage from a broken pipe on Pine Avenue.", R.drawable.photo),
        Report("Overflowing trash bin", "Park Lane", "2024-06-23", "The trash bin on Park Lane is overflowing and needs to be emptied.", R.drawable.photo),
        Report("Damaged Swing", "Central Park", "2024-06-22", "Broken swing in Central Park playground.", R.drawable.photo),
        Report("Blocked drain", "Oak Street", "2024-06-21", "Blocked drain causing water to pool on Oak Street.", R.drawable.photo),
        Report("Uncollected garbage", "Maple Street", "2024-06-20", "Garbage has not been collected on Maple Street for over a week.", R.drawable.photo),
        Report("Fallen tree branch", "Birch Road", "2024-06-19", "A large tree branch has fallen on Birch Road and is blocking traffic.", R.drawable.photo),
        Report("Broken bench", "Riverwalk", "2024-06-18", "A bench along the Riverwalk is broken and needs repair.", R.drawable.photo)
    )
}

@Preview(showBackground = true)
@Composable
fun ViewReportsPreview() {
    val navController = rememberNavController()
    ViewReports(navController)
}
