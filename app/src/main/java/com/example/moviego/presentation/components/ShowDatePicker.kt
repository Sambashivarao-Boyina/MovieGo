package com.example.moviego.presentation.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDatePicker(
    selectedDate: String,
    onSelected: (date: String) -> Unit,
    error: String
) {
    val calendar = Calendar.getInstance()

    // Set the maximum selectable date (today + 7 days)
    val maxDateCalendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 6)
    }

    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
            onSelected(formattedDate)
        },
        currentYear,
        currentMonth,
        currentDay
    ).apply {
        datePicker.minDate = calendar.timeInMillis // Today
        datePicker.maxDate = maxDateCalendar.timeInMillis // Today + 7 days
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = selectedDate.ifEmpty { "Select the Date" },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(30.dp),
                    color = Color.Gray
                )
                .padding(20.dp)
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
            )
        }
    }
}
