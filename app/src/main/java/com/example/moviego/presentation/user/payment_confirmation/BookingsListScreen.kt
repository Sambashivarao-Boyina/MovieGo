package com.example.moviego.presentation.user.payment_confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviego.domain.model.Booking
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedBB0
import com.example.moviego.ui.theme.RedE31
import java.text.SimpleDateFormat
import java.util.*


// Additional red shades for variety
private val RedA33 = Color(0xFFAA3333)

@Composable
fun BookingListScreen(bookings: List<Booking>) {
    // Calculate stats
    val totalBookings = bookings.size
    val totalSeats = bookings.sumOf { it.seats.size }
    val totalRevenue = bookings.sumOf { it.totalBookingCost }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black111)
            .padding(20.dp)
    ) {


        // Enhanced Stats Cards
        EnhancedStatsSection(totalBookings, totalSeats, totalRevenue)

        Spacer(modifier = Modifier.height(28.dp))

        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Bookings",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (bookings.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Black1C1
                ) {
                    Text(
                        text = "${bookings.size} total",
                        color = RedE31,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bookings list
        if (bookings.isEmpty()) {
            EmptyBookingsView()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bookings) { booking ->
                    EnhancedBookingCard(booking = booking)
                }
                item {
                    Spacer(Modifier.height(200.dp))
                }
            }
        }
    }
}

@Composable
fun EnhancedStatsSection(totalBookings: Int, totalSeats: Int, totalRevenue: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Total Bookings Card
        StatCard(
            title = "Total Bookings",
            value = totalBookings.toString(),
            backgroundColor = RedE31,
            modifier = Modifier.weight(1f)
        )

        // Total Seats Card
        StatCard(
            title = "Total Seats",
            value = totalSeats.toString(),
            backgroundColor = RedBB0,
            modifier = Modifier.weight(1f)
        )

        // Revenue Card
        StatCard(
            title = "Revenue",
            value = "₹${"%.0f".format(totalRevenue)}",
            backgroundColor = RedA33,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EnhancedBookingCard(booking: Booking) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Black161,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header Row with ID and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Booking #${booking._id.take(8).uppercase()}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatDate(booking.createdAt),
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                EnhancedStatusChip(status = booking.bookingStatus)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Information Section
            UserInfoSection(booking.user.email, booking.user.phone)

            Spacer(modifier = Modifier.height(16.dp))

            // Seats and Payment Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Column - Seats
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Seats",
                        color = RedE31,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val seatsText = booking.seats.joinToString(", ") { it.seatCode }
                    Text(
                        text = seatsText,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${booking.seats.size} seat${if (booking.seats.size > 1) "s" else ""}",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                // Right Column - Payment
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total Amount",
                        color = RedE31,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${"%.2f".format(booking.totalBookingCost)}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "₹${"%.2f".format(booking.ticketCost)} per seat",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            // Payment ID Section
            if (booking.paymentId != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Payment ID: ",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = booking.paymentId,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun UserInfoSection(email: String, phone: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Black1C1
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(RedE31),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = email.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = email,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = phone,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EnhancedStatusChip(status: String) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "success" -> Pair(Color(0xFF2E7D32), Color.White)
        "failed" -> Pair(RedBB0, Color.White)
        "processing" -> Pair(Color(0xFFF57C00), Color.White)
        "pending" -> Pair(Color(0xFF9E9E9E), Color.White)
        else -> Pair(Color(0xFF616161), Color.White)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Text(
            text = status.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun EmptyBookingsView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = Black1C1,
            modifier = Modifier.size(80.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📋",
                    fontSize = 32.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Bookings Found",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Bookings will appear here once they are made",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}