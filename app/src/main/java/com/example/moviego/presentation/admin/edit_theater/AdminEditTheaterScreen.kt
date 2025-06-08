package com.example.moviego.presentation.admin.edit_theater

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.moviego.R
import com.example.moviego.presentation.admin.add_theater.AdminAddTheaterEvent
import com.example.moviego.presentation.admin.add_theater.Root
import com.example.moviego.presentation.admin.add_theater.readRawResource
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.util.Constants.getFileFromUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun AdminEditTheaterScreen(
    editTheaterState: EditTheaterState,
    isLoading: Boolean,
    onEvent: (AdminEditTheaterEvent) -> Unit,
    isSuccess: Boolean,
    navController: NavHostController
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(isSuccess) {
        if(isSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("refresh",true)
            navController.popBackStack()
        }
    }

    val gson = remember {
        Gson()
    }
    val states = remember {
        val statesJson = readRawResource(R.raw.states, context)
        gson.fromJson<List<String>>(statesJson, object : TypeToken<List<String>>() {}.type)
    }

    val citiesMap = remember {
        val jsonString = readRawResource(R.raw.state_cities, context)
        gson.fromJson(jsonString, Root::class.java)
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val file = getFileFromUri(context, uri)
                onEvent(AdminEditTheaterEvent.UpdateImage(file))
            }

        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
            if(permissionGranted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(context, "Permission is Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    var showMapPointSelection by remember {
        mutableStateOf(false)
    }

    var locationPermissionGranted by remember {
        mutableStateOf(false)
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (locationPermissionGranted) {

            } else {
                Toast.makeText(context, "Location permission is required to select a point on the map", Toast.LENGTH_SHORT).show()
            }
        }
    )



    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(80.6400, 15.9129)) // Andhra Pradesh coordinates
            zoom(8.0)
        }
    }

    var pickedLocation by remember { mutableStateOf<Point?>(null) }

    var mapStyle by remember{
        mutableStateOf(Style.STANDARD)
    }

    LaunchedEffect(key1 = pickedLocation) {
        pickedLocation?.let {
            Log.d("location","Picked location: ${it.latitude()}, ${it.longitude()}")
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    Scaffold(
        topBar = {
            TopBar(
                title = "Add Theater",
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
        if(showMapPointSelection) {
            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize()) {

                // Map View
                MapboxMap(
                    Modifier.fillMaxSize(),
                    mapViewportState = mapViewportState,
                    onMapClickListener = { clickedPoint ->
                        pickedLocation = clickedPoint
                        true
                    },
                    style = { MapStyle(style = mapStyle) }
                ) {
                    MapEffect(Unit) { mapView ->
                        mapView.location.updateSettings {
                            locationPuck = createDefault2DPuck(withBearing = true)
                            enabled = true
                            puckBearing = PuckBearing.COURSE
                            puckBearingEnabled = true
                        }
                        mapViewportState.transitionToFollowPuckState()
                    }

                    pickedLocation?.let { point ->
                        val marker = rememberIconImage(
                            key = R.drawable.theater,
                            painter = painterResource(R.drawable.theater)
                        )

                        PointAnnotation(point = point) {
                            iconImage = marker
                            iconSize = 0.2
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 70.dp, end = 20.dp)
                        .zIndex(1f), // Ensures it's above Map
                    contentAlignment = Alignment.BottomStart
                ) {

                    IconButton(onClick = { expanded = true }, modifier = Modifier.align(Alignment.BottomStart)) {
                        Icon(
                            painter = painterResource(R.drawable.map_type),
                            contentDescription = "Menu",
                            modifier = Modifier.size(100.dp),
                            tint = Color.Red // Ensure visibility depending on your theme
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Standard") },
                            onClick = {
                                expanded = false
                                mapStyle = Style.STANDARD
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Satellite") },
                            onClick = {
                                expanded = false
                                mapStyle = Style.SATELLITE
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Traffic Day") },
                            onClick = {
                                expanded = false
                                mapStyle = Style.TRAFFIC_DAY
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Standard") },
                            onClick = {
                                expanded = false
                                mapStyle = Style.STANDARD_EXPERIMENTAL
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Streets") },
                            onClick = {
                                expanded = false
                                mapStyle = Style.MAPBOX_STREETS
                            }
                        )
                    }

                }

                // Button at Bottom End
                Button(
                    onClick = {
                        pickedLocation?.let {
                            onEvent(
                                AdminEditTheaterEvent.UpdateCoordinates(
                                    latitude = it.latitude(),
                                    longitude = it.longitude()
                                )
                            )
                            showMapPointSelection = false
                        }
                    },
                    enabled = pickedLocation != null,
                    colors = ButtonDefaults.buttonColors().copy(
                        disabledContainerColor = Color.Gray
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text("Set Location")
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(it)
                    .padding(horizontal = 10.dp)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        InputBox(
                            value = editTheaterState.name,
                            onChange = {
                                onEvent(AdminEditTheaterEvent.UpdateName(it))
                            },
                            placeHolder = "Enter Name",
                            error = editTheaterState.nameError,
                            leadingIcon = null,
                            keyboardType = KeyboardType.Text,
                        )
                    }

                    item {
                        InputBox(
                            value = editTheaterState.contactNumber,
                            onChange = {
                                onEvent(AdminEditTheaterEvent.UpdateContactNumber(it))
                            },
                            placeHolder = "Enter Contact Number",
                            error = editTheaterState.contactNumberError,
                            leadingIcon = null,
                            keyboardType = KeyboardType.Phone,
                        )
                    }

                    item {
                        DropDownSelect(
                            items = states.map { item ->
                                DropDownItem(
                                    title = item,
                                    ref = item
                                )
                            },
                            onSelect = {
                                onEvent(AdminEditTheaterEvent.UpdateState(it.title))
                            },
                            error = editTheaterState.stateError,
                            unAvailableMessage = "Select the State",
                            unSelectedMessage = "Select the State",
                            initialValue = DropDownItem(
                                title = editTheaterState.state,
                                ref = editTheaterState.state
                            )
                        )
                    }

                    item {
                        InputBox(
                            value = editTheaterState.pincode,
                            onChange = {
                                onEvent(AdminEditTheaterEvent.UpdatePincode(it))
                            },
                            placeHolder = "Enter PinCode",
                            error = editTheaterState.pincodeError,
                            leadingIcon = null,
                            keyboardType = KeyboardType.Number,
                        )
                    }

                    item {
                        DropDownSelect(
                            items = citiesMap.states.find { it.state == editTheaterState.state }
                                ?.cities
                                ?.map { DropDownItem(title = it.city, ref = it.city) }
                                ?: emptyList(),
                            onSelect = {
                                onEvent(AdminEditTheaterEvent.UpdateCity(it.title))
                            },
                            error = editTheaterState.cityError,
                            unAvailableMessage = "Select the State",
                            unSelectedMessage = "Select the City"
                        )
                    }

                    item {
                        InputBox(
                            value = editTheaterState.address,
                            onChange = {
                                onEvent(AdminEditTheaterEvent.UpdateAddress(it))
                            },
                            placeHolder = "Enter Address",
                            error = editTheaterState.addressError,
                            leadingIcon = null,
                            keyboardType = KeyboardType.Text,
                        )
                    }

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
                            if (editTheaterState.image != null) {
                                // Show selected image
                                Image(
                                    painter = rememberAsyncImagePainter(editTheaterState.image),
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

                        if(editTheaterState.imageError.isNotEmpty()) {
                            Text(
                                text = editTheaterState.imageError, color = Color.Red, modifier = Modifier.padding(
                                    start = 20.dp, top = 10.dp
                                )
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                showMapPointSelection = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if(editTheaterState.latitude != 0.0 && editTheaterState.longitude != 0.0) "Log : ${editTheaterState.longitude} - Lat : ${editTheaterState.latitude}" else "Pick Location",
                            )
                        }
                        if (editTheaterState.coordinateError.isEmpty()) {
                            Text(
                                text = editTheaterState.coordinateError,
                                color = Color.Red,
                                modifier = Modifier.padding(
                                    start = 20.dp, top = 10.dp
                                )
                            )
                        }

                    }

                }
                Spacer(Modifier.height(20.dp))
                SubmitButton(
                    onClick = {
                        onEvent(AdminEditTheaterEvent.SubmitEditTheater)
                    },
                    loading = isLoading,
                    title = "Submit"
                )
            }
        }
    }
}