package com.example.moviego.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moviego.ui.theme.Black1C1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownSelect(
    items: List<DropDownItem>,
    onSelect: (DropDownItem) -> Unit,
    error: String,
    unAvailableMessage: String,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember(items) { mutableStateOf<DropDownItem?>(null) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (items.isNotEmpty()) expanded = it } // Only expand if items exist
        ) {
            OutlinedTextField(
                value = selectedItem?.title ?: if (items.isEmpty()) unAvailableMessage else "No Option Selected" ,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Choose an option") },
                enabled = items.isNotEmpty(), // Disable TextField if empty
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray
                ),
                shape = CircleShape,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
                    .background(Black1C1)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.title) },
                        onClick = {
                            selectedItem = item
                            expanded = false
                            onSelect(item)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
            )
        }
    }
}


data class DropDownItem(
    val title:String,
    val ref: String
)