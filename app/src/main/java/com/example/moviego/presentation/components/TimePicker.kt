package com.example.moviego.presentation.components

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(
    selectedTime : String,
    onSelected: (time: String) -> Unit,
    error: String
) {


    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hourOfDay, minute ->
            val hour = if (hourOfDay == 0 || hourOfDay == 12) 12 else hourOfDay % 12
            val amPm = if (hourOfDay < 12) "AM" else "PM"
            onSelected(String.format("%02d:%02d %s", hour, minute, amPm))
        },
        currentHour,
        currentMinute,
        false // Set to false for 12-hour format with AM/PM
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = selectedTime.ifEmpty { "Select the Time" },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    timePickerDialog.show()
                }
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(30.dp),
                    color = Color.Gray
                )
                .padding(20.dp)
        )
        if(error.isNotEmpty()) {
            Text(
                text = error, color = Color.Red, modifier = Modifier.padding(
                    start = 20.dp, top = 10.dp
                )
            )
        }
    }


}
