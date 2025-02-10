package com.example.moviego.presentation.authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.moviego.R

@Composable
fun PasswordInput(
    value: String,
    onChange:(String) -> Unit,
    placeholder: String,
    error: String = ""
) {
    var showPassword by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            placeholder = {
                Text(text = placeholder)
            },
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        showPassword = !showPassword
                    }
                ) {
                    if (showPassword) {
                        Icon(painter = painterResource(R.drawable.show), contentDescription = null)
                    } else {
                        Icon(painter = painterResource(R.drawable.hide), contentDescription = null)
                    }
                }
            },
            visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(mask = '*')
        )
        if(error.isNotEmpty()){
            Text(
                text = error, color = Color.Red, modifier = Modifier.padding(
                    start = 20.dp, top = 10.dp
                )
            )
        }
    }
}