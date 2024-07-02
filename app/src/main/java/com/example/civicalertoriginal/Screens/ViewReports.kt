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

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draw.alpha
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class Report(
    val incidentType: String = "",
    val location: String = "",
    val dateTime: String = "",
    val description: String = "",
    val imageRes: Int = R.drawable.photo // Replace with actual drawable resource ID
)
enum class FilterOption {
    Recent, // Sorting by recent
    Old // Sorting from oldest to latest
}
fun fetchReportsFromFirebase(reports: SnapshotStateList<Report>) {
    val database = Firebase.database
    val reportsRef = database.getReference("Make Report Instance")

    reportsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            reports.clear()
            for (reportSnapshot in snapshot.children) {
                val report = reportSnapshot.getValue(Report::class.java)
                if (report != null) {
                    reports.add(report)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle database error
        }
    })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReports(navController: NavController) {
    val reports = remember { mutableStateListOf<Report>() }
    fetchReportsFromFirebase(reports)

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

            val filteredReports = when (filterOption) {
                FilterOption.Recent -> reports.sortedByDescending { it.dateTime }
                FilterOption.Old -> reports.sortedBy { it.dateTime }
            }.filter {
                it.incidentType.contains(searchText, ignoreCase = true) || it.location.contains(searchText, ignoreCase = true)
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 16.dp, end = 12.dp)
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
                            placeholder = { Text("Search", color = Color.Black) },
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
            label = { Text(label, color = Color.Black) },
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
                        text = report.incidentType,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold,color = Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.location,
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = report.dateTime,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                      if (!expanded)  Text(
                            text = report.description,
                            modifier = Modifier
                                .height(24.dp)
                                .clipToBounds(),// Show only the first 10 letters
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                        else Text(
                          text = report.description, modifier = Modifier.alpha(0f) )
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
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
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


@Preview(showBackground = true)
@Composable
fun ViewReportsPreview() {
    val navController = rememberNavController()
    ViewReports(navController)
}
