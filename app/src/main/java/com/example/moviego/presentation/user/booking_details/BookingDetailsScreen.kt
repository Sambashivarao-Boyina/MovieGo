package com.example.moviego.presentation.user.booking_details

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Booking
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.RedE31
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Composable
fun BookingDetailsScreen(
    isLoading: Boolean,
    booking: Booking?,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Booking Details",
                navigationBox = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(LayoutDirection.Ltr),
                    end = it.calculateEndPadding(LayoutDirection.Rtl)
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {

                val context = LocalContext.current
                val viewRef = remember { mutableStateOf<View?>(null) }

                booking?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {


                            AndroidView(factory = { ctx ->
                                FrameLayout(ctx).apply {
                                    val composeView = ComposeView(ctx).apply {
                                        setContent {
                                            Column(
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .background(Black111)
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .border(
                                                            width = 1.dp,
                                                            shape = RoundedCornerShape(10.dp),
                                                            color = RedE31
                                                        ),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Spacer(
                                                        Modifier
                                                            .height(10.dp)
                                                            .fillMaxWidth()
                                                            .clip(
                                                                RoundedCornerShape(
                                                                    topStart = 20.dp,
                                                                    topEnd = 20.dp
                                                                )
                                                            )
                                                            .background(RedE31)
                                                    )
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                horizontal = 20.dp,
                                                                vertical = 12.dp
                                                            ),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Column {
                                                            Text(
                                                                text = booking.show.movie.Title,
                                                                style = MaterialTheme.typography.displaySmall,
                                                                color = RedE31,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                            Spacer(Modifier.height(10.dp))
                                                            Text(
                                                                text = "${booking.show.movie.Language} ",
                                                                color = Color.Gray
                                                            )
                                                            Text(
                                                                text = "${booking.show.movie.Genre}",
                                                                color = Color.Gray
                                                            )
                                                        }
                                                        Column(
                                                            horizontalAlignment = Alignment.End,
                                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                                        ) {
                                                            Text(
                                                                text = booking.bookingStatus.uppercase(),
                                                                style = MaterialTheme.typography.titleSmall,
                                                                modifier = Modifier
                                                                    .clip(RoundedCornerShape(20.dp))
                                                                    .background(RedE31)
                                                                    .padding(
                                                                        horizontal = 10.dp,
                                                                        vertical = 6.dp
                                                                    ),
                                                                fontWeight = FontWeight.SemiBold
                                                            )
                                                            Row(
                                                                verticalAlignment = Alignment.Top
                                                            ) {
                                                                Text(
                                                                    text = "IMDb  ",
                                                                    color = Color.Gray
                                                                )

                                                                Text(
                                                                    text = "${booking.show.movie.imdbRating}/10",
                                                                    fontWeight = FontWeight.SemiBold,
                                                                    color = Color.Yellow
                                                                )
                                                            }
                                                        }
                                                    }

                                                    Row(
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .background(Color.Black)
                                                            .padding(horizontal = 0.dp)
                                                    ) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                .data(booking.show.movie.Poster)
                                                                .placeholder(R.drawable.placeholder)
                                                                .error(R.drawable.placeholder)
                                                                .allowHardware(false)
                                                                .build(),
                                                            contentDescription = "poster",
                                                            modifier = Modifier
                                                                .fillMaxHeight()
                                                                .weight(0.4f)
                                                        )
                                                        Column(
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .padding(20.dp),
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            )
                                                        ) {
                                                            IconHeadline(
                                                                icon = R.drawable.calender,
                                                                text = "${booking.show.date}"
                                                            )
                                                            IconHeadline(
                                                                icon = R.drawable.clock_outline,
                                                                text = "${booking.show.showTime}"
                                                            )
                                                            IconHeadline(
                                                                icon = R.drawable.ticket,
                                                                text = "RUNTIME: ${booking.show.movie.Runtime}"
                                                            )
                                                            Text(
                                                                text = "Director: ${booking.show.movie.Director}",
                                                                color = Color.Gray
                                                            )
                                                            Text(
                                                                text = "Stars: ${booking.show.movie.Actors}",
                                                                color = Color.Gray
                                                            )
                                                        }
                                                    }

                                                    Spacer(Modifier.height(10.dp))
                                                    DashedDivider(color = RedE31)
                                                    Spacer(Modifier.height(10.dp))
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                horizontal = 20.dp,
                                                                vertical = 5.dp
                                                            ),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Column(
                                                            modifier = Modifier
                                                                .height(80.dp)
                                                                .width(2.dp)
                                                                .background(RedE31)
                                                        ) {

                                                        }
                                                        Column(
                                                            modifier = Modifier.padding(horizontal = 20.dp),
                                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                                        ) {
                                                            IconHeadline(
                                                                icon = R.drawable.location,
                                                                text = "${booking.show.theater.name}"
                                                            )
                                                            Text(
                                                                text = "${booking.show.theater.city}, ${booking.show.theater.address},${booking.show.theater.state}, ${booking.show.theater.pincode}",
                                                                color = Color.Gray
                                                            )
                                                        }
                                                    }

                                                    Spacer(Modifier.height(10.dp))

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceEvenly
                                                    ) {
                                                        TheaterInfo(
                                                            icon = R.drawable.ticket,
                                                            title = "Screen",
                                                            value = booking.show.screen.screenName
                                                        )
                                                        TheaterInfo(
                                                            icon = R.drawable.screen,
                                                            title = "Display",
                                                            value = booking.show.screen.soundType
                                                        )
                                                        TheaterInfo(
                                                            icon = R.drawable.headphones,
                                                            title = "Sound",
                                                            value = booking.show.screen.soundType
                                                        )
                                                    }
                                                    Spacer(Modifier.height(10.dp))
                                                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.4f))
                                                    Spacer(Modifier.height(10.dp))

                                                    Column(
                                                        modifier = Modifier.padding(horizontal = 20.dp)
                                                    ) {
                                                        IconHeadline(
                                                            icon = R.drawable.seat,
                                                            text = "Seats"
                                                        )
                                                        Spacer(Modifier.height(6.dp))
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.Center,
                                                        ) {
                                                            for (seat in booking.seats) {
                                                                Text(
                                                                    text = seat.seatCode,
                                                                    fontWeight = FontWeight.Bold,
                                                                    modifier = Modifier
                                                                        .clip(RoundedCornerShape(10.dp))
                                                                        .background(Color.Red)
                                                                        .padding(10.dp)
                                                                )
                                                                Spacer(Modifier.width(5.dp))
                                                            }
                                                        }
                                                    }

                                                    Spacer(modifier = Modifier.height(30.dp))

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                    ) {
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            )
                                                        ) {
                                                            Text("Payment ID", color = Color.Gray)
                                                            Text("Booking ID", color = Color.Gray)
                                                        }
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            ),
                                                            horizontalAlignment = Alignment.End
                                                        ) {
                                                            Text(text = booking.paymentId ?: "")
                                                            Text(text = booking._id)
                                                        }
                                                    }

                                                    Spacer(Modifier.height(10.dp))
                                                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.4f))
                                                    Spacer(Modifier.height(10.dp))

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.9f)
                                                            .clip(RoundedCornerShape(10.dp))
                                                            .background(Color.Black)
                                                            .padding(20.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                    ) {
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            )
                                                        ) {
                                                            Text("Tickets", color = Color.Gray)
                                                            Text("Convenience Fee", color = Color.Gray)
                                                        }
                                                        Column(
                                                            verticalArrangement = Arrangement.spacedBy(
                                                                10.dp
                                                            ),
                                                            horizontalAlignment = Alignment.End
                                                        ) {
                                                            Text(text = "₹${(booking.ticketCost * booking.seats.size)}")
                                                            Text(text = "₹${(booking.ticketCost * booking.seats.size) * 0.1}")
                                                        }
                                                    }

                                                    Spacer(Modifier.height(10.dp))

                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 20.dp),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = "Total Amount:",
                                                            style = MaterialTheme.typography.titleLarge,
                                                            color = Color.Gray
                                                        )
                                                        Text(
                                                            text = "₹${booking.totalBookingCost}",
                                                            style = MaterialTheme.typography.titleLarge,
                                                        )
                                                    }

                                                    Spacer(Modifier.height(20.dp))
                                                }
                                            }
                                        }
                                    }
                                    addView(composeView)
                                    viewRef.value = composeView
                                }
                            }, modifier = Modifier.fillMaxWidth())
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
//
                                        viewRef.value?.let { it1 ->
                                            saveViewAsImage(
                                                it1,
                                                context
                                            )
                                        }

                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RedE31,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(
                                        10.dp
                                    )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.download),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            "Download",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        // Construct the URI

                                       booking?.let {
                                           val uri = Uri.parse("geo:${it.show.theater.location.coordinates[1]},${it.show.theater.location.coordinates[0]}?q=${it.show.theater.location.coordinates[1]},${it.show.theater.location.coordinates[0]}")

                                           // Create an Intent to open the Maps app
                                           val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                               setPackage("com.google.android.apps.maps")
                                           }

                                           // Check if the Maps app is available
                                           if (intent.resolveActivity(context.packageManager) != null) {
                                               context.startActivity(intent)
                                           } else {
                                               Toast.makeText(context, "Google Maps app not found", Toast.LENGTH_SHORT).show()
                                           }
                                       }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Gray.copy(alpha = 0.3f),
                                        contentColor = RedE31
                                    ),
                                    shape = RoundedCornerShape(
                                        10.dp
                                    )
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.location),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            "Navigate",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun IconHeadline(
    icon: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = RedE31,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun DashedDivider(
    color: Color = Color.Gray,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 10.dp,
    gapLength: Dp = 5.dp,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val stroke = with(LocalDensity.current) {
        Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength.toPx(), gapLength.toPx())
            )
        )
    }

    Canvas(modifier = modifier.height(strokeWidth)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = stroke.width,
            pathEffect = stroke.pathEffect
        )
    }
}


@Composable
fun TheaterInfo(
    icon: Int,
    title: String,
    value: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = RedE31,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = title,
            color = Color.Gray,
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

    }

}



private val STORAGE_PERMISSION_REQUEST_CODE = 1001

fun saveViewAsImage(view: View, context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    view.draw(canvas)

    val filename = "column_capture_${System.currentTimeMillis()}.png"
    val mimeType = "image/png"
    val downloadsDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadsDir, filename)

    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Make it visible to gallery
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            arrayOf(mimeType),
            null
        )

        Toast.makeText(context, "Image saved to Downloads", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
}
