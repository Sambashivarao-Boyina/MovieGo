package com.example.moviego.presentation.admin.movie_details

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Movie
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminMovieDetailsScreen(
    movie: Movie?,
    isLoading: Boolean,
    navigateBack: Boolean,
    navController: NavHostController
) {
    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            navController.popBackStack()
        }
    }
    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        movie?.let {
            Scaffold {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = it.calculateBottomPadding(), start = it.calculateLeftPadding(
                                layoutDirection = LayoutDirection.Ltr
                            ), end = it.calculateEndPadding(LayoutDirection.Rtl)
                        ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(600.dp)
                        ) {
                            // Movie poster
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(movie.Poster)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "${movie.Title} poster",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color(0xFF121212).copy(alpha = 0.5f),
                                                Color(0xFF121212)
                                            ),
                                            startY = 0f,
                                            endY = with(LocalDensity.current) { 500.dp.toPx() }
                                        )
                                    )
                            )

                            // Back button
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier
                                    .padding(30.dp)
                                    .size(40.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }

                            // Movie info at bottom of poster
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = movie.Title,
                                    style = MaterialTheme.typography.displaySmall,
                                    color = Color.White
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = movie.Year,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.LightGray
                                    )
                                    Text(
                                        text = movie.Runtime,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.LightGray
                                    )
                                    Text(
                                        text = movie.Language,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.LightGray
                                    )
                                }

                                // Rating
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                ) {
                                    Surface(
                                        color = RedE31,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = movie.imdbRating,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                    }
                                    Text(
                                        text = "IMDb",
                                        fontSize = 14.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.padding(start = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            movie.Genre.split(",").map {
                                Text(
                                    text = it,
                                    color = RedE31,
                                    modifier = Modifier
                                        .border(
                                            2.dp,
                                            RedE31.copy(alpha = 0.8f),
                                            RoundedCornerShape(20.dp)
                                        )
                                        .clip(
                                            RoundedCornerShape(20.dp)
                                        )
                                        .background(RedE31.copy(alpha = 0.2f))
                                        .padding(10.dp)
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "Story", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = movie.Plot,
                                color = Color.Gray
                            )
                        }
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.weight(0.5f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "Director", color = Color.Gray )
                                    Text(text = movie.Director)
                                }
                                Column(
                                    modifier = Modifier.weight(0.5f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "Writer", color = Color.Gray )
                                    Text(text = movie.Writer)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.weight(0.5f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "Stars", color = Color.Gray )
                                    Text(text = movie.Actors)
                                }
                                Column(
                                    modifier = Modifier.weight(0.5f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "Country", color = Color.Gray )
                                    Text(text = movie.Country)
                                }
                            }
                        }

                    }

                    item {
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}