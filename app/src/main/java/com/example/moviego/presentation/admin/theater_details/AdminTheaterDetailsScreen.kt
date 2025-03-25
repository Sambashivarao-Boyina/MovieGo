package com.example.moviego.presentation.admin.theater_details

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviego.R
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.components.ScreenCard
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedE31

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AdminTheaterDetailsScreen(
    theaterDetails: TheaterDetails?,
    isLoading: Boolean,
    navController: NavHostController,
    screenState: ScreenState,
    onEvent:(AdminTheaterDetailsEvent)-> Unit,
    editScreenState: ScreenState
) {

    var screenExpanded by remember { mutableStateOf(false) }
    var selectedScreenType by remember { mutableStateOf(screenTypes[0]) }

    var soundExpanded by remember { mutableStateOf(false) }
    var selectedSoundType by remember { mutableStateOf(soundSystems[0]) }
    Scaffold(
        topBar = {
            TopBar(
                title = "Theater Details",
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
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(AdminTheaterDetailsEvent.UpdateShowNewScreen(true))
                },
                shape = CircleShape,
                containerColor = RedE31
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ){
            if(isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            if(!isLoading && theaterDetails == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Data is Empty"
                    )
                }
            }

            theaterDetails?.let {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),

                    ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Box {
                                // Image Background
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(theaterDetails.image)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .build(),
                                    contentDescription = theaterDetails.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                // Gradient Overlay & Text
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomStart)
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(Color.Transparent, Color.Black),
                                                startY = 100f
                                            )
                                        )
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = theaterDetails.name,
                                            style = TextStyle(
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = RedE31
                                            )
                                        )
                                        Text(
                                            text = "${theaterDetails.city}, ${theaterDetails.state}",
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }


                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(modifier = Modifier.background(Black1C1).padding(16.dp)) {

                                DetailRow(label = "Contact Number", value = theaterDetails.contactNumber)
                                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                                DetailRow(label = "Address", value = theaterDetails.address)
                                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                                DetailRow(label = "City", value = theaterDetails.city)
                                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                                DetailRow(label = "State", value = theaterDetails.state)
                                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

                                DetailRow(label = "Pincode", value = theaterDetails.pincode)
                            }
                        }
                    }


                    item {
                        Text(text = "Screens(${theaterDetails.screens.size})", style = MaterialTheme.typography.titleLarge)
                    }

                    items(theaterDetails.screens) { screen ->
                        ScreenCard(screen, onEvent)
                    }


                }
            }

            if(screenState.showNewScreen) {
                Dialog(
                    onDismissRequest = {
                        onEvent(AdminTheaterDetailsEvent.UpdateShowNewScreen(false))
                    },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = true,
                        dismissOnClickOutside = false,
                        dismissOnBackPress = true
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Black1C1)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InputBox(
                            value = screenState.screenName,
                            placeHolder = "Enter Screen Name",
                            onChange = {
                                onEvent(AdminTheaterDetailsEvent.UpdateScreenName(it))
                            },
                            keyboardType = KeyboardType.Text,
                            error = screenState.isScreenNameError
                        )

                        Column {
                            ExposedDropdownMenuBox(
                                expanded = screenExpanded,
                                onExpandedChange = { screenExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = screenState.screenType,
                                    onValueChange = {},
                                    readOnly = true,
                                    shape = CircleShape,
                                    colors = TextFieldDefaults.colors().copy(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedIndicatorColor = Color.Gray,
                                        unfocusedIndicatorColor = Color.Gray
                                    ),
                                    placeholder = { Text("Select Screen Type") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = screenExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    modifier = Modifier.background(Black111),
                                    expanded = screenExpanded,
                                    onDismissRequest = { screenExpanded = false }
                                ) {
                                    screenTypes.forEach { screenType ->
                                        DropdownMenuItem(
                                            text = { Text(screenType) },
                                            onClick = {
                                                onEvent(AdminTheaterDetailsEvent.UpdateScreenType(screenType))
                                                screenExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (screenState.isScreenTypeError.isNotEmpty()) {
                                Text(
                                    text = screenState.isScreenTypeError, color = Color.Red, modifier = Modifier.padding(
                                        start = 20.dp, top = 10.dp
                                    )
                                )
                            }
                        }
                        Column {

                            ExposedDropdownMenuBox(
                                expanded = soundExpanded,
                                onExpandedChange = { soundExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = screenState.soundType,
                                    onValueChange = {},
                                    readOnly = true,
                                    shape = CircleShape,
                                    colors = TextFieldDefaults.colors().copy(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedIndicatorColor = Color.Gray,
                                        unfocusedIndicatorColor = Color.Gray
                                    ),
                                    placeholder = { Text("Select Sound System") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = soundExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    modifier = Modifier.background(Black111),
                                    expanded = soundExpanded,
                                    onDismissRequest = { soundExpanded = false }
                                ) {
                                    soundSystems.forEach { soundType ->
                                        DropdownMenuItem(
                                            text = { Text(soundType) },
                                            onClick = {
                                                onEvent(AdminTheaterDetailsEvent.UpdateSoundType(soundType))

                                                soundExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            if (screenState.isSoundTypeError.isNotEmpty()) {
                                Text(
                                    text = screenState.isSoundTypeError, color = Color.Red, modifier = Modifier.padding(
                                        start = 20.dp, top = 10.dp
                                    )
                                )
                            }
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    onEvent(AdminTheaterDetailsEvent.UpdateShowNewScreen(false))
                                },
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = Black111,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Cancel", style = MaterialTheme.typography.titleMedium)
                            }

                            Button(
                                onClick = {
                                    onEvent(AdminTheaterDetailsEvent.SubmitScreen)
                                },
                                shape = CircleShape
                            ) {
                                if(screenState.submittingScreen) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
                                } else {
                                    Text(text = "Submit", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }
                }
            }
            if(editScreenState.showNewScreen) {
                Dialog(
                    onDismissRequest = {
                        onEvent(AdminTheaterDetailsEvent.UpdateShowEditScreen(false))
                    },
                    properties = DialogProperties(
                        usePlatformDefaultWidth = true,
                        dismissOnClickOutside = false,
                        dismissOnBackPress = true
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Black1C1)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InputBox(
                            value = editScreenState.screenName,
                            placeHolder = "Enter Screen Name",
                            onChange = {
                                onEvent(AdminTheaterDetailsEvent.UpdateEditScreenName(it))
                            },
                            keyboardType = KeyboardType.Text,
                            error = editScreenState.isScreenNameError
                        )

                        Column {
                            ExposedDropdownMenuBox(
                                expanded = screenExpanded,
                                onExpandedChange = { screenExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = editScreenState.screenType,
                                    onValueChange = {},
                                    readOnly = true,
                                    shape = CircleShape,
                                    colors = TextFieldDefaults.colors().copy(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedIndicatorColor = Color.Gray,
                                        unfocusedIndicatorColor = Color.Gray
                                    ),
                                    placeholder = { Text("Select Screen Type") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = screenExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    modifier = Modifier.background(Black111),
                                    expanded = screenExpanded,
                                    onDismissRequest = { screenExpanded = false }
                                ) {
                                    screenTypes.forEach { screenType ->
                                        DropdownMenuItem(
                                            text = { Text(screenType) },
                                            onClick = {
                                                onEvent(AdminTheaterDetailsEvent.UpdateEditScreenType(screenType))
                                                screenExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (editScreenState.isScreenTypeError.isNotEmpty()) {
                                Text(
                                    text = editScreenState.isScreenTypeError, color = Color.Red, modifier = Modifier.padding(
                                        start = 20.dp, top = 10.dp
                                    )
                                )
                            }
                        }
                        Column {

                            ExposedDropdownMenuBox(
                                expanded = soundExpanded,
                                onExpandedChange = { soundExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = editScreenState.soundType,
                                    onValueChange = {},
                                    readOnly = true,
                                    shape = CircleShape,
                                    colors = TextFieldDefaults.colors().copy(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedIndicatorColor = Color.Gray,
                                        unfocusedIndicatorColor = Color.Gray
                                    ),
                                    placeholder = { Text("Select Sound System") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = soundExpanded)
                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    modifier = Modifier.background(Black111),
                                    expanded = soundExpanded,
                                    onDismissRequest = { soundExpanded = false }
                                ) {
                                    soundSystems.forEach { soundType ->
                                        DropdownMenuItem(
                                            text = { Text(soundType) },
                                            onClick = {
                                                onEvent(AdminTheaterDetailsEvent.UpdateEditSoundType(soundType))

                                                soundExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            if (editScreenState.isSoundTypeError.isNotEmpty()) {
                                Text(
                                    text = editScreenState.isSoundTypeError, color = Color.Red, modifier = Modifier.padding(
                                        start = 20.dp, top = 10.dp
                                    )
                                )
                            }
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    onEvent(AdminTheaterDetailsEvent.UpdateShowEditScreen(false))
                                },
                                colors = ButtonDefaults.buttonColors().copy(
                                    containerColor = Black111,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Cancel", style = MaterialTheme.typography.titleMedium)
                            }

                            Button(
                                onClick = {
                                    onEvent(AdminTheaterDetailsEvent.SubmitEditScreen)
                                },
                                shape = CircleShape
                            ) {
                                if(screenState.submittingScreen) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(30.dp))
                                } else {
                                    Text(text = "Submit", style = MaterialTheme.typography.titleMedium)
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
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

val screenTypes = listOf(
    "Standard 2D Screen", "IMAX Screen", "Dolby Cinema Screen", "3D Screen",
    "4DX Screen", "ScreenX", "LED Cinema Screen", "Laser Projection Screen",
    "Drive-in Screen", "Dome/Planetarium Screen"
)

val soundSystems = listOf(
    "Dolby Digital", "Dolby Atmos", "DTS", "DTS:X", "Auro 11.1",
    "IMAX Sound System", "THX Sound System", "7.1 Surround Sound",
    "5.1 Surround Sound", "Meyer Sound System"
)