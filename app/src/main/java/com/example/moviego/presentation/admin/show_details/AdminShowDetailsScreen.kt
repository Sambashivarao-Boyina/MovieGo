package com.example.moviego.presentation.admin.show_details

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.moviego.R
import com.example.moviego.domain.model.Booking
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Screen
import com.example.moviego.domain.model.Seat
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.Theater
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.components.shimmerEffect
import com.example.moviego.presentation.user.payment_confirmation.BookingListScreen
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.RedE31
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminShowDetailsScreen(
    showDetails: ShowDetails?,
    isLoading: Boolean,
    bookings: List<Booking>,
    isBookingsLoading: Boolean,
    onEvent: (AdminShowDetailsEvent) -> Unit,
    navController: NavHostController,
    updatingShowStatus: Boolean
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    var currentOption by remember {
        mutableStateOf<Option>(Option.Details)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar(
                title = "Show Details",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .zIndex(1f), // Ensures it's above Map
                            contentAlignment = Alignment.BottomStart
                        ) {

                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.align(Alignment.BottomStart)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Menu",
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {

                                if (showDetails != null) {
                                    if(currentOption == Option.Details) {
                                        if (showDetails.bookingStatus == "Open") {
                                            DropdownMenuItem(
                                                text = { Text("Close Bookings") },
                                                onClick = {
                                                    expanded = false
                                                    onEvent(AdminShowDetailsEvent.CloseShow)
                                                }
                                            )
                                        } else {
                                            DropdownMenuItem(
                                                text = { Text("Open Bookings") },
                                                onClick = {
                                                    expanded = false
                                                    onEvent(AdminShowDetailsEvent.OpenShow)
                                                }
                                            )
                                        }
                                    } else {
                                        DropdownMenuItem(
                                            text = { Text("Print Bookings") },
                                            onClick = {
                                                scope.launch {
                                                    generateBookingsPDF(context, bookings)
                                                }
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )

        }
    ) {
        Box(
            modifier = Modifier.padding(it).fillMaxSize()
        ) {
            if (currentOption == Option.Details) {
                if(isLoading) {
                    AdminShowDetailsLoadingShimmer()
                } else {
                    if(showDetails != null){
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                MovieInfoHeader(
                                    movie = showDetails.movie,
                                    ticketCost = showDetails.ticketCost,
                                    status = showDetails.bookingStatus
                                )
                            }

                            // Theater & Show Info
                            item {
                                TheaterShowInfo(
                                    theater = showDetails.theater,
                                    screen = showDetails.screen,
                                    date = showDetails.date,
                                    showTime = showDetails.showTime
                                )
                            }

                            // Booking Status
                            item {
                                BookingStatus(
                                    totalSeats = showDetails.seats.size,
                                    bookedCount = showDetails.bookedSeatsCount
                                )
                            }

                            // Seat Status Legend
                            item {
                                SeatLegend()
                            }



                            item {
                                TheaterScreen()
                            }

                            item {
                                SeatMap(seats = showDetails.seats)
                            }

                            item {
                                Spacer(Modifier.height(200.dp))
                            }

                        }
                    }
                }
            } else {
                if(isBookingsLoading) {
                    AdminShowBookingsLoadingShimmer()
                } else {
                    BookingListScreen(bookings)
                }

            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .background(
                            Black161
                        )
                        .border(
                            width = 2.dp,
                            color = RedE31,
                            shape = CircleShape
                        )
                        .padding(
                            10.dp
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .width(110.dp)
                            .clip(CircleShape)
                            .clickable {
                                currentOption = Option.Details
                            }
                            .background(
                                if(currentOption == Option.Details ) RedE31 else Color.Transparent
                            )
                            .padding(vertical = 5.dp),
                        text = "Details",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .width(110.dp)
                            .clip(CircleShape)
                            .clickable {
                                currentOption = Option.Bookings
                            }
                            .background(
                                if(currentOption == Option.Bookings ) RedE31 else Color.Transparent
                            )
                            .padding(vertical = 5.dp),
                        text = "Bookings",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


        if (updatingShowStatus) {
            LoadingDialog()
        }
    }
}



@Composable
private fun MovieInfoHeader(movie: Movie, ticketCost: Int, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            Box {
                // Background gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Enhanced Movie Poster
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp
                    ) {
                        AsyncImage(
                            model = movie.Poster,
                            contentDescription = "${movie.Title} Poster",
                            modifier = Modifier
                                .width(120.dp)
                                .height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Movie Details
                    Column(modifier = Modifier.weight(1f)) {
                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when (status.lowercase()) {
                                "open" -> Color(0xFF4CAF50)
                                "closed" -> Color(0xFFE53E3E)
                                else -> MaterialTheme.colorScheme.primary
                            }
                        ) {
                            Text(
                                text = status.uppercase(),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = movie.Title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        MovieInfoRow(
                            icon = R.drawable.time,
                            label = "Duration",
                            value = "${movie.Runtime}"
                        )

                        MovieInfoRow(
                            icon = R.drawable.language,
                            label = "Language",
                            value = movie.Language
                        )

                        MovieInfoRow(
                            icon = R.drawable.rupee,
                            label = "Ticket Cost",
                            value = "₹$ticketCost"
                        )
                    }
                }
            }

            // IMDB Rating and Genre
            if (movie.imdbRating.isNotEmpty() && movie.imdbRating != "N/A") {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = movie.imdbRating,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "/10 IMDB",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = movie.Genre,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieInfoRow(icon: Int, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TheaterShowInfo(theater: Theater, screen: Screen, date: String, showTime: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Theater Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Theater Details
            TheaterInfoItem(
                label = "Theater",
                value = theater.name,
                icon = R.drawable.theater_icon
            )

            TheaterInfoItem(
                label = "Address",
                value = "${theater.address}, ${theater.city}, ${theater.state} - ${theater.pincode}",
                icon = R.drawable.location
            )

            TheaterInfoItem(
                label = "Screen",
                value = "${screen.screenName} (${screen.screenType}, ${screen.soundType})",
                icon = R.drawable.screen
            )

            TheaterInfoItem(
                label = "Date & Time",
                value = "$date at $showTime",
                icon = R.drawable.calender
            )

            TheaterInfoItem(
                label = "Contact",
                value = theater.contactNumber,
                icon = R.drawable.phone
            )
        }
    }
}

@Composable
private fun TheaterInfoItem(label: String, value: String, icon: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(16.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BookingStatus(bookedCount: Int, totalSeats: Int) {
    val progressPercentage = (bookedCount.toFloat() / totalSeats.toFloat())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Booking Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(progressPercentage * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = progressPercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "$bookedCount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Booked",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${totalSeats - bookedCount}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Available",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SeatLegend() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(
                color = MaterialTheme.colorScheme.surface,
                borderColor = MaterialTheme.colorScheme.outline,
                text = "Available"
            )
            LegendItem(
                color = Color(0xFFE53E3E),
                borderColor = Color(0xFFE53E3E),
                text = "Booked"
            )
            LegendItem(
                color = Color(0xFFFF9800),
                borderColor = Color(0xFFFF9800),
                text = "Processing"
            )
        }
    }
}

@Composable
private fun LegendItem(color: Color, borderColor: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(24.dp),
            shape = RoundedCornerShape(6.dp),
            color = color,
            border = BorderStroke(2.dp, borderColor)
        ) {}

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TheaterScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "SCREEN",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        ) {
            val width = size.width
            val height = size.height

            drawPath(
                path = Path().apply {
                    moveTo(width * 0.1f, height)
                    quadraticTo(
                        width / 2,
                        -height * 2,
                        width * 0.9f,
                        height
                    )
                    lineTo(width * 0.9f, height)
                    lineTo(width * 0.1f, height)
                    close()
                },
                color = Color(0xFFE53E3E)
            )
        }
    }
}

@Composable
private fun SeatMap(seats: List<Seat>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        seats.chunked(20).forEachIndexed { rowIndex, rowSeats ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                // Row Label
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = (rowIndex + 65).toChar().toString(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                rowSeats.forEachIndexed { columnIndex, seat ->
                    if (columnIndex == 10) {
                        Spacer(modifier = Modifier.width(32.dp))
                    }
                    SeatComponent(seat)
                }
            }
        }
    }
}

@Composable
private fun SeatComponent(seat: Seat) {
    val seatColor = when (seat.status.lowercase()) {
        "booked" -> Color(0xFFE53E3E)
        "processing" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = when (seat.status.lowercase()) {
        "booked" -> Color(0xFFE53E3E)
        "processing" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.outline
    }

    Surface(
        modifier = Modifier.size(28.dp),
        shape = RoundedCornerShape(6.dp),
        color = seatColor,
        border = BorderStroke(1.5.dp, borderColor),
        shadowElevation = if (seat.status != "available") 2.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (seat.status == "Booked") {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Text(text = seat.seatCode, fontSize = 10.sp)
            }
        }
    }
}


@Composable
private fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Updating Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Please wait...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
enum class Option {
    Details,
    Bookings
}



suspend fun generateBookingsPDF(context: Context, bookings: List<Booking>): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas
            var pageNumber = 1

            // Dark theme paint objects
            val titlePaint = Paint().apply {
                color = android.graphics.Color.parseColor("#DC143C")
                textSize = 32f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }

            val whitePaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 14f
                isAntiAlias = true
            }

            val grayLightPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#B0B0B0")
                textSize = 12f
                isAntiAlias = true
            }

            val redAccentPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#DC143C")
                textSize = 13f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            fun drawNewPage(): Canvas {
                if (pageNumber > 1) {
                    pdfDocument.finishPage(page)
                }
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                pageNumber++
                return canvas
            }

            fun drawPageBackground(canvas: Canvas) {
                // Dark background for entire page
                val bgPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#1a1a1a")
                }
                canvas.drawRect(0f, 0f, 595f, 842f, bgPaint)
            }

            fun drawHeader(canvas: Canvas) {
                // Gradient-like header with multiple rectangles
                val headerHeight = 100f

                // Main header background
                val headerRect = RectF(0f, 0f, 595f, headerHeight)
                val headerPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#0d1117")
                }
                canvas.drawRect(headerRect, headerPaint)

                // Red accent strip
                val accentRect = RectF(0f, headerHeight - 8f, 595f, headerHeight)
                val accentPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#DC143C")
                }
                canvas.drawRect(accentRect, accentPaint)

                // Title with glow effect
                val titleShadowPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#40DC143C")
                    textSize = 32f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                    maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
                }
                canvas.drawText("BOOKING REPORTS", 297.5f, 42f, titleShadowPaint)
                canvas.drawText("BOOKING REPORTS", 297.5f, 40f, titlePaint)

                // Subtitle info
                val subPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#8b949e")
                    textSize = 11f
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                canvas.drawText("Generated: ${SimpleDateFormat("dd MMMM yyyy • HH:mm", Locale.getDefault()).format(Date())}", 297.5f, 65f, subPaint)
                canvas.drawText("Total Records: ${bookings.size}", 297.5f, 80f, subPaint)
            }

            fun drawBookingCard(canvas: Canvas, booking: Booking, yPos: Float, cardIndex: Int): Float {
                var yPosition = yPos
                val cardMargin = 30f
                val cardWidth = 535f
                val cardLeft = cardMargin
                val cardRight = cardLeft + cardWidth
                val cardHeight = 220f + (booking.seats.size * 16f)

                // Outer glow/shadow
                val glowPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#20DC143C")
                    maskFilter = BlurMaskFilter(6f, BlurMaskFilter.Blur.NORMAL)
                }
                val glowRect = RectF(cardLeft - 2, yPosition - 2, cardRight + 2, yPosition + cardHeight + 2)
                canvas.drawRoundRect(glowRect, 16f, 16f, glowPaint)

                // Main card background - Dark theme
                val cardRect = RectF(cardLeft, yPosition, cardRight, yPosition + cardHeight)
                val cardPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#21262d")
                }
                canvas.drawRoundRect(cardRect, 14f, 14f, cardPaint)

                // Card border
                val borderPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#30363d")
                    style = Paint.Style.STROKE
                    strokeWidth = 1f
                }
                canvas.drawRoundRect(cardRect, 14f, 14f, borderPaint)

                // Header section
                val headerHeight = 45f
                val headerRect = RectF(cardLeft, yPosition, cardRight, yPosition + headerHeight)
                val headerBgPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#DC143C")
                }
                // Draw header with rounded top corners only
//                val path = Path()
//                path.addRoundRect(headerRect, floatArrayOf(14f, 14f, 14f, 14f, 0f, 0f, 0f, 0f), Path.Direction.CW)
//                canvas.drawPath(path, headerBgPaint)

                // Booking number and ID in header
                val headerTextPaint = Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 14f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    isAntiAlias = true
                }
                canvas.drawText("BOOKING #${cardIndex + 1}", cardLeft + 20f, yPosition + 18f, headerTextPaint.apply { textSize = 11f })
                canvas.drawText(booking._id, cardLeft + 20f, yPosition + 35f, headerTextPaint.apply { textSize = 13f })

                // Status badge with rounded corners
                val statusWidth = 80f
                val statusHeight = 22f
                val statusRect = RectF(cardRight - statusWidth - 15f, yPosition + 12f, cardRight - 15f, yPosition + 12f + statusHeight)
                val statusBgPaint = Paint().apply {
                    color = when(booking.bookingStatus) {
                        "Success" -> android.graphics.Color.parseColor("#238636")
                        "Pending" -> android.graphics.Color.parseColor("#bf8700")
                        "Failed" -> android.graphics.Color.parseColor("#da3633")
                        else -> android.graphics.Color.parseColor("#6e7681")
                    }
                }
                canvas.drawRoundRect(statusRect, 6f, 6f, statusBgPaint)

                val statusTextPaint = Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 10f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                canvas.drawText(booking.bookingStatus.uppercase(), cardRight - statusWidth/2 - 15f, yPosition + 26f, statusTextPaint)

                yPosition += headerHeight + 20f

                // Content sections with better spacing
                val contentLeft = cardLeft + 20f
                val contentRight = cardRight - 20f

                // Section: Customer Details
                val sectionHeaderPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#DC143C")
                    textSize = 11f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    isAntiAlias = true
                }

                val labelPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#8b949e")
                    textSize = 10f
                    isAntiAlias = true
                }

                val valuePaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#f0f6fc")
                    textSize = 11f
                    isAntiAlias = true
                }

                canvas.drawText("CUSTOMER", contentLeft, yPosition, sectionHeaderPaint)
                yPosition += 16f

                canvas.drawText("Email", contentLeft + 8f, yPosition, labelPaint)
                canvas.drawText(booking.user.email, contentLeft + 60f, yPosition, valuePaint)
                yPosition += 14f

                canvas.drawText("Phone", contentLeft + 8f, yPosition, labelPaint)
                canvas.drawText(booking.user.phone, contentLeft + 60f, yPosition, valuePaint)
                yPosition += 20f

                // Section: Payment
                canvas.drawText("PAYMENT", contentLeft, yPosition, sectionHeaderPaint)
                yPosition += 16f

                canvas.drawText("Payment ID", contentLeft + 8f, yPosition, labelPaint)
                canvas.drawText(booking.paymentId ?: "No Payment Id", contentLeft + 80f, yPosition, valuePaint)
                yPosition += 20f

                // Section: Seats
                canvas.drawText("SEATS", contentLeft, yPosition, sectionHeaderPaint)
                yPosition += 16f

                booking.seats.forEach { seat ->
                    val seatStatusColor = when(seat.status) {
                        "Booked" -> android.graphics.Color.parseColor("#238636")
                        "Processing" -> android.graphics.Color.parseColor("#bf8700")
                        else -> android.graphics.Color.parseColor("#6e7681")
                    }

                    // Seat bullet
                    val bulletPaint = Paint().apply {
                        color = seatStatusColor
                    }
                    canvas.drawCircle(contentLeft + 12f, yPosition - 4f, 3f, bulletPaint)

                    canvas.drawText(seat.seatCode, contentLeft + 20f, yPosition, valuePaint)

                    val statusPaint = Paint().apply {
                        color = seatStatusColor
                        textSize = 9f
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        isAntiAlias = true
                    }
                    canvas.drawText(seat.status.uppercase(), contentLeft + 60f, yPosition, statusPaint)
                    yPosition += 14f
                }

                yPosition += 10f

                // Pricing section with background
                val pricingRect = RectF(contentLeft - 8f, yPosition - 8f, contentRight + 8f, yPosition + 28f)
                val pricingBgPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#161b22")
                }
                canvas.drawRoundRect(pricingRect, 8f, 8f, pricingBgPaint)

                val pricingBorderPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#30363d")
                    style = Paint.Style.STROKE
                    strokeWidth = 1f
                }
                canvas.drawRoundRect(pricingRect, 8f, 8f, pricingBorderPaint)

                canvas.drawText("PRICING", contentLeft, yPosition + 8f, sectionHeaderPaint)

                val ticketPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#f0f6fc")
                    textSize = 11f
                    isAntiAlias = true
                }
                canvas.drawText("Ticket: ₹${booking.ticketCost}", contentLeft + 8f, yPosition + 22f, ticketPaint)

                val totalPaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#DC143C")
                    textSize = 13f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    isAntiAlias = true
                }
                val totalText = "Total: ₹${booking.totalBookingCost}"
                val totalWidth = totalPaint.measureText(totalText)
                canvas.drawText(totalText, contentRight - totalWidth + 8f, yPosition + 22f, totalPaint)

                yPosition += 40f

                // Date footer
                val datePaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#6e7681")
                    textSize = 9f
                    isAntiAlias = true
                }
                canvas.drawText("Booked: ${formatDate(booking.createdAt)}", contentLeft, yPosition, datePaint)

                return yPosition + 25f
            }

            // Draw page background and header
            drawPageBackground(canvas)
            drawHeader(canvas)
            var yPosition = 120f

            bookings.forEachIndexed { index, booking ->
                val estimatedCardHeight = 280f

                // Check if we need a new page
                if (yPosition + estimatedCardHeight > 800f) {
                    canvas = drawNewPage()
                    drawPageBackground(canvas)
                    drawHeader(canvas)
                    yPosition = 120f
                }

                yPosition = drawBookingCard(canvas, booking, yPosition, index) + 15f
            }

            // Footer
            val footerPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#6e7681")
                textSize = 9f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            canvas.drawText("Booking Management System • Page $pageNumber", 297.5f, 825f, footerPaint)

            pdfDocument.finishPage(page)

            // Save PDF
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val fileName = "BookingsReport_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.pdf"
            val file = File(downloadsDir, fileName)

            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
            fileOutputStream.close()
            pdfDocument.close()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}


@Composable
fun AdminShowDetailsLoadingShimmer() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.height(150.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )
        Box(
            modifier = Modifier.height(150.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )
        Box(
            modifier = Modifier.height(50.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )

    }
}

@Composable
fun AdminShowBookingsLoadingShimmer() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for(k in 1..3) {
                Box(
                    modifier = Modifier.weight(1f).height(130.dp).clip(RoundedCornerShape(20.dp)).shimmerEffect()
                )
            }
        }
        Box(
            modifier = Modifier.height(20.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )
        Box(
            modifier = Modifier.height(150.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )

        Box(
            modifier = Modifier.height(150.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp)).shimmerEffect()
        )


    }
}