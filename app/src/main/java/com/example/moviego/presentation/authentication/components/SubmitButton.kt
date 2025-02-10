package com.example.moviego.presentation.authentication.components

import android.widget.Button
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SubmitButton(
    title:String,
    loading:Boolean,
    onClick:()->Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
            .height(50.dp),
        shape = CircleShape
    ) {
        if(loading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
        } else {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
    }
}