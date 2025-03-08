package com.example.moviego.presentation.authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
@Composable
fun InputBox(
    value: String,
    onChange: (String) -> Unit,
    placeHolder: String,
    error: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardType: KeyboardType
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            leadingIcon = leadingIcon?.let { { it() } }, // Only add if not null
            placeholder = {
                Text(text = placeHolder)
            },
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            )
        )
        if (error.isNotEmpty()) {
            Text(
                text = error, color = Color.Red, modifier = Modifier.padding(
                    start = 20.dp, top = 10.dp
                )
            )
        }
    }
}