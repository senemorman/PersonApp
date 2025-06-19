package com.project.personapp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.personapp.room.Person
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: PersonViewModel) {

    // Observe LiveData from ViewModel
    val contacts by viewModel.users.observeAsState(emptyList())
    val inputName by viewModel.inputName.observeAsState("")
    val inputSurname by viewModel.inputSurname.observeAsState("")
    val inputDepartment by viewModel.inputDepartment.observeAsState("")
    val inputTime by viewModel.inputTime.observeAsState("")
    val inputPhone by viewModel.inputPhone.observeAsState("")

    val saveButtonText by viewModel.saveOrUpdateButtonText.observeAsState("Save")
    val clearButtonText by viewModel.clearAllOrDeleteButtonText.observeAsState("Clear All")
    var showDialog by remember { mutableStateOf(false) }
    var dialogPerson by remember { mutableStateOf<Person?>(null) }
    var inputHours by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Title
        Text(
            text = "Person Manager",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Input Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Name Input
                OutlinedTextField(
                    value = inputName ?: "",
                    onValueChange = { viewModel.inputName.value = it },
                    label = { Text("Person Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // surname Input
                OutlinedTextField(
                    value = inputSurname ?: "",
                    onValueChange = { viewModel.inputSurname.value = it },
                    label = { Text("Person surname") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // Department Input
                OutlinedTextField(
                    value = inputDepartment ?: "",
                    onValueChange = { viewModel.inputDepartment.value = it },
                    label = { Text("Person Department") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // Time Input
                OutlinedTextField(
                    value = inputTime ?: "",
                    onValueChange = { viewModel.inputTime.value = it },
                    label = { Text("Person available hours") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                // Phone Input
                OutlinedTextField(
                    value = inputPhone ?: "",
                    onValueChange = { viewModel.inputPhone.value = it },
                    label = { Text("Person Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Save/Update Button
                    Button(
                        onClick = { viewModel.saveOrUpdate() },
                        modifier = Modifier.weight(1f),
                        enabled = !inputName.isNullOrBlank() && !inputSurname.isNullOrBlank()
                    ) {
                        Text(saveButtonText ?: "Save")
                    }

                    // Clear All/Delete Button
                    OutlinedButton(
                        onClick = { viewModel.clearAllOrDelete() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(clearButtonText ?: "Clear All")
                    }
                }
            }
        }

        // Persons List
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Persons (${contacts.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (contacts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No persons yet.\nAdd your first persons above!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(contacts) { contact ->
                            ContactItem(
                                person = contact,
                                onEditClick = { viewModel.initUpdateAndDelete(contact) },
                                onDeleteClick = { viewModel.delete(contact) },
                                onClick = { selectedPerson ->
                                    showDialog = true
                                    dialogPerson = selectedPerson
                                }
                            )
                        }

                    }
                }
            }
        }
    }
    if (showDialog && dialogPerson != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                inputHours = ""
            },
            confirmButton = {
                TextButton(onClick = {
                    val subtractHours = inputHours.toIntOrNull() ?: 0
                    val currentHours = dialogPerson?.contact_time?.toIntOrNull() ?: 0
                    val newHours = (currentHours - subtractHours).coerceAtLeast(0)

                    // Update the person in the DB
                    dialogPerson?.let {
                        val updated = it.copy(contact_time = newHours.toString())
                        viewModel.update(updated)
                    }

                    showDialog = false
                    inputHours = ""
                }) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    inputHours = ""
                }) {
                    Text("Cancel")
                }
            },
            title = { Text("Change Availability") },
            text = {
                Column {
                    Text("How many hours is this person unavailable?")
                    OutlinedTextField(
                        value = inputHours,
                        onValueChange = { inputHours = it },
                        label = { Text("Hours to subtract") },
                        singleLine = true
                    )
                }
            }
        )
    }


}

@Composable
fun ContactItem(
    person: Person,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClick: (Person) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(person) }, // 👈 handle click
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Name: " + person.contact_name + " " + person.contact_surname,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Department: " + person.contact_department,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val context = LocalContext.current
                ClickableText(
                    text = AnnotatedString("Phone Number: ${person.contact_phone}"),
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${person.contact_phone}")
                        }
                        context.startActivity(intent)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary, // optional: make it look like a link
                        fontWeight = FontWeight.Medium
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Circle indicator
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = if (person.contact_time != null) Color(0xFF4CAF50) else Color(0xFFD32F2F),
                                shape = CircleShape
                            )
                    )
                    // Status text (optional)
                    Text(
                        text = "Available for: " + "${person.contact_time} hours",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Contact",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Contact",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}