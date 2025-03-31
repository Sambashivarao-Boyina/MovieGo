package com.example.moviego.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.Movie
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31

@Composable
fun MovieCard(movie: Movie, onClick:()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Black1C1
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black1C1)
        ) {
            // Movie Poster and Details Layout
            Column(

            ) {
                // Movie Poster
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2/3f)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.Poster)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .build(),
                        contentDescription = movie.Title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Rating Overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                color = RedE31.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                    ) {
                        Text(
                            text = movie.imdbRating,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Movie Details
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 10.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                ) {
                    // Movie Title
                    Text(
                        text = movie.Title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Movie Year and Genre
                    Text(
                        text = "${movie.Year} • ${movie.Genre}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}



// Preview for Design Testing
@Preview(showBackground = true)
@Composable
fun MovieCardPreview() {
    val sampleMovie = Movie(
        Title = "Inception",
        Year = "2010",
        Genre = "Sci-Fi, Action",
        Poster = "https://example.com/poster.jpg",
        imdbRating = "8.8",
        Runtime = "148 min",
        Country = "USA",
        Actors = "",
        Awards = "",
        BoxOffice = "",
        DVD = "",
        Director = "",
        Language = "",
        Metascore = "",
        Plot = "",
        Production = "",
        Rated = "",
        Ratings = listOf(),
        Released = "",
        Response = "",
        Type = "",
        Website = "",
        Writer = "",
        _id = "",
        imdbID = "",
        imdbVotes = ""
    )

    MovieCard(
        movie = sampleMovie,
        onClick ={}
    )
}