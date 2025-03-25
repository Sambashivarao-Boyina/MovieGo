package com.example.moviego.presentation.admin.add_theater

import android.Manifest
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.moviego.R
import com.example.moviego.presentation.admin.components.TopBar
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.presentation.components.DropDownItem
import com.example.moviego.presentation.components.DropDownSelect
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.util.Constants.getFileFromUri

@Composable
fun AdminAddTheaterScreen(
    newTheaterState: NewTheaterState,
    onEvent: (AdminAddTheaterEvent) -> Unit,
    navController: NavHostController,
    isLoading: Boolean,
    isSuccess: Boolean
) {

    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(isSuccess) {
        if(isSuccess) {
            navController.popBackStack()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val file = getFileFromUri(context, uri)
                onEvent(AdminAddTheaterEvent.UpdateImage(file))
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
                        value = newTheaterState.name,
                        onChange = {
                            onEvent(AdminAddTheaterEvent.UpdateName(it))
                        },
                        placeHolder = "Enter Name",
                        error = newTheaterState.nameError,
                        leadingIcon = null,
                        keyboardType = KeyboardType.Text,
                    )
                }

                item {
                    InputBox(
                        value = newTheaterState.contactNumber,
                        onChange = {
                            onEvent(AdminAddTheaterEvent.UpdateContactNumber(it))
                        },
                        placeHolder = "Enter Contact Number",
                        error = newTheaterState.contactNumberError,
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
                            onEvent(AdminAddTheaterEvent.UpdateState(it.title))
                        },
                        error = newTheaterState.stateError,
                        unAvailableMessage = "Select the State",
                        unSelectedMessage = "Select the State"
                    )
                }

                item {
                    InputBox(
                        value = newTheaterState.pincode,
                        onChange = {
                            onEvent(AdminAddTheaterEvent.UpdatePincode(it))
                        },
                        placeHolder = "Enter PinCode",
                        error = newTheaterState.pincodeError,
                        leadingIcon = null,
                        keyboardType = KeyboardType.Number,
                    )
                }

                item {
                    InputBox(
                        value = newTheaterState.city,
                        onChange = {
                            onEvent(AdminAddTheaterEvent.UpdateCity(it))
                        },
                        placeHolder = "Enter City",
                        error = newTheaterState.cityError,
                        leadingIcon = null,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    InputBox(
                        value = newTheaterState.address,
                        onChange = {
                            onEvent(AdminAddTheaterEvent.UpdateAddress(it))
                        },
                        placeHolder = "Enter Address",
                        error = newTheaterState.addressError,
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
                        if (newTheaterState.image != null) {
                            // Show selected image
                            Image(
                                painter = rememberAsyncImagePainter(newTheaterState.image),
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

                    if(newTheaterState.imageError.isNotEmpty()) {
                        Text(
                            text = newTheaterState.imageError, color = Color.Red, modifier = Modifier.padding(
                                start = 20.dp, top = 10.dp
                            )
                        )
                    }
                }

            }
            Spacer(Modifier.height(20.dp))
            SubmitButton(
                onClick = {
                    onEvent(AdminAddTheaterEvent.SubmitNewTheater)
                },
                loading = isLoading,
                title = "Submit"
            )
        }
    }
}

var states: Array<String> = arrayOf("Andhra Pradesh",
    "Arunachal Pradesh",
    "Assam",
    "Bihar",
    "Chhattisgarh",
    "Goa",
    "Gujarat",
    "Haryana",
    "Himachal Pradesh",
    "Jharkhand",
    "Karnataka",
    "Kerala",
    "Madhya Pradesh",
    "Maharashtra",
    "Manipur",
    "Meghalaya",
    "Mizoram",
    "Nagaland",
    "Odisha",
    "Punjab",
    "Rajasthan",
    "Sikkim",
    "Tamil Nadu",
    "Telangana",
    "Tripura",
    "Uttar Pradesh",
    "Uttarakhand",
    "West Bengal",
    "Andaman and Nicobar Islands",
    "Chandigarh",
    "Dadra and Nagar Haveli and Daman and Diu",
    "Lakshadweep",
    "Delhi",
    "Puducherry",
    "Ladakh",
    "Jammu and Kashmir")