package com.example.moviego.presentation.admin.admin_add_movie

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.moviego.R
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.util.Constants.getFileFromUri
import java.io.File

@Composable
fun AdminAddMovieScreen(
    addMovieState: MovieState,
    isLoading: Boolean,
    onEvent: (AdminAddMovieEvent) -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val file = getFileFromUri(context, uri)
                onEvent(AdminAddMovieEvent.UpdatePosterImage(file))
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            permissionGranted = isGranted
            if(isGranted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold (
        topBar = {
            TopBar(
                title = "Add Movie",
                navigationBox = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(it).padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Black1C1, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                launcher.launch("image/*")
                            } else {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (addMovieState.posterImage != null) {
                        // Show selected image
                        Image(
                            painter = rememberAsyncImagePainter(addMovieState.posterImage),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(painter = painterResource(R.drawable.upload), contentDescription = null, modifier = Modifier.size(50.dp))
                            Text(text = "Upload Poster")
                        }
                    }
                }

                if(addMovieState.isPosterImageError.isNotEmpty()) {
                    Text(
                        text = addMovieState.isPosterImageError, color = Color.Red, modifier = Modifier.padding(
                            start = 20.dp, top = 10.dp
                        )
                    )
                }
            }

            item {
                InputBox(
                    value = addMovieState.title,
                    onChange = {
                        onEvent(AdminAddMovieEvent.UpdateTitle(it))
                    },
                    placeHolder = "Enter Movie Name",
                    keyboardType = KeyboardType.Text,
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.movie), contentDescription = "movie", modifier = Modifier.size(25.dp))
                    },
                    error = addMovieState.isTitleError
                )
            }
            item {
                InputBox(
                    value = addMovieState.duration.toString(),
                    onChange = {
                        val duration = it.toIntOrNull()
                        if(duration != null) {
                            onEvent(AdminAddMovieEvent.UpdateDuration(duration))
                        } else {
                            onEvent(AdminAddMovieEvent.UpdateDuration(0))
                        }
                    },
                    placeHolder = "Enter Duration of Movie",
                    keyboardType = KeyboardType.Number,
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.time), contentDescription = "duration", modifier = Modifier.size(25.dp))
                    },
                    error = addMovieState.isDurationError
                )
            }
            item {
                InputBox(
                    value = addMovieState.language,
                    onChange = {
                        onEvent(AdminAddMovieEvent.UpdateLanguage(it))
                    },
                    placeHolder = "Enter Language",
                    keyboardType = KeyboardType.Text,
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.language), contentDescription = "language", modifier = Modifier.size(25.dp))
                    },
                    error = addMovieState.isLanguageError
                )
            }

            item{
                SubmitButton(
                    title = "Add",
                    loading = isLoading,
                    onClick = {
                        onEvent(AdminAddMovieEvent.SubmitMovie)
                    }
                )
            }
        }
    }


}

