package com.example.moviego.presentation.admin.admin_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.domain.model.Admin
import com.example.moviego.presentation.authentication.components.PasswordInput
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.ui.theme.Black111
import com.example.moviego.ui.theme.Black161
import com.example.moviego.ui.theme.Black1C1
import com.example.moviego.ui.theme.RedBB0
import com.example.moviego.ui.theme.RedE31

@Composable
fun AdminDetailsScreen(
    admin:Admin?,
    isLoading: Boolean,
    changePhoneNumber: ChangePhoneNumber,
    changePassword: ChangePassword,
    onEvent: (AdminDetailsEvent) -> Unit,
    navController: NavHostController
) {

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            admin?.let {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){


                    item {
                        Image(
                            painter = painterResource(R.drawable.profile_placeholder),
                            contentDescription = "Profile",
                            modifier = Modifier.size(100.dp)
                                .clip(RoundedCornerShape(50)),
                            contentScale = ContentScale.Fit,

                            )

                        Text(
                            text = admin.name,
                            style = MaterialTheme.typography.displaySmall,
                            color = RedE31,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Text(
                            text = admin.email,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            OutlinedTextField(
                                value = admin.phone,
                                readOnly = true,
                                onValueChange = {

                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone number")
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            onEvent(AdminDetailsEvent.UpdatePhoneNumberPopup(true))
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = RedE31)
                                    }
                                },
                                colors = TextFieldDefaults.colors().copy(
                                    focusedContainerColor = Black1C1,
                                    unfocusedContainerColor = Black1C1,
                                    unfocusedIndicatorColor = Black111,
                                    focusedIndicatorColor = Black111
                                )
                            )
                        }


                        if(changePhoneNumber.isOpenPopup) {
                            Dialog(
                                onDismissRequest = {
                                    onEvent(AdminDetailsEvent.UpdatePhoneNumberPopup(false))

                                },
                                properties = DialogProperties(
                                    dismissOnClickOutside = true,
                                    dismissOnBackPress = true,
                                    usePlatformDefaultWidth = true
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Black1C1)
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "Change Phone Number", style = MaterialTheme.typography.titleMedium)

                                    OutlinedTextField(
                                        value = changePhoneNumber.newPhoneNumber,
                                        onValueChange = {
                                            onEvent(AdminDetailsEvent.UpdatePhoneNumber(it))
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone number")
                                        },
                                        colors = TextFieldDefaults.colors().copy(
                                            focusedContainerColor = Black111,
                                            unfocusedContainerColor = Black111,
                                            unfocusedIndicatorColor = Black111,
                                            focusedIndicatorColor = Black111
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Phone
                                        )
                                    )

                                    if(changePhoneNumber.isError.isNotEmpty()) {
                                        Text(changePhoneNumber.isError, color = RedE31)
                                    }

                                    Row (
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(top = 30.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                onEvent(AdminDetailsEvent.UpdatePhoneNumberPopup(false))

                                            },
                                            colors = ButtonDefaults.buttonColors().copy(
                                                contentColor = RedE31,
                                                containerColor = Black161
                                            )
                                        ) {
                                            Text("Cancel")
                                        }
                                        Spacer(modifier = Modifier.width(20.dp))
                                        Button(
                                            onClick = {
                                                onEvent(AdminDetailsEvent.SubmitNewPhoneNumber)
                                            },
                                            enabled = !changePhoneNumber.isChangingPhone,
                                            colors = ButtonDefaults.buttonColors().copy(
                                                containerColor = RedE31,
                                                disabledContainerColor = RedBB0,
                                                contentColor = Color.White,
                                                disabledContentColor = Color.White
                                            ),
                                        ) {
                                            if(changePhoneNumber.isChangingPhone) {
                                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                            } else {
                                                Text("Submit")
                                            }
                                        }


                                    }
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 10.dp)
                                .clickable {
                                    navController.navigate(Route.AdminMovies.route)
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .background(Black1C1)
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,

                                ) {
                                Text("Your Movies", color = RedE31, style = MaterialTheme.typography.titleLarge)
                                Icon(imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null , tint = RedE31)
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(10.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 10.dp)
                                .clickable {
                                    navController.navigate(Route.AdminTheaters.route)
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .background(Black1C1)
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,

                                ) {
                                Text("Your Theaters", color = RedE31, style = MaterialTheme.typography.titleLarge)
                                Icon(imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null, tint = RedE31)

                            }
                        }
                    }


                    item {
                        Spacer(Modifier.height(10.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 10.dp)
                                .clickable {
                                    onEvent(AdminDetailsEvent.UpdatePasswordPopup(true))
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .background(Black1C1),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,

                                ) {
                                Text("Change Password", color = RedE31, style = MaterialTheme.typography.titleLarge)
                            }
                        }

                        if(changePassword.isOpenPopup) {
                            Dialog(
                                onDismissRequest = {
                                    onEvent(AdminDetailsEvent.UpdatePasswordPopup(false))
                                },
                                properties = DialogProperties(
                                    dismissOnClickOutside = true,
                                    dismissOnBackPress = true,
                                    usePlatformDefaultWidth = true
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Black1C1)
                                        .padding(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(text = "Change Password", style = MaterialTheme.typography.titleMedium)

                                    PasswordInput(
                                        value = changePassword.newPassword,
                                        onChange = {
                                            onEvent(AdminDetailsEvent.UpdateNewPassword(it))
                                        },
                                        placeholder = "New Password",
                                        error = changePassword.isNewPasswordError
                                    )

                                    PasswordInput(
                                        value = changePassword.confirmPassword,
                                        onChange = {
                                            onEvent(AdminDetailsEvent.UpdateConfirmPassword(it))
                                        },
                                        placeholder = "Confirm Password",
                                        error = changePassword.isConfirmPasswordError
                                    )

                                    Row (
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(top = 30.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            onClick = {
                                                onEvent(AdminDetailsEvent.UpdatePasswordPopup(false))

                                            },
                                            colors = ButtonDefaults.buttonColors().copy(
                                                contentColor = RedE31,
                                                containerColor = Black161
                                            )
                                        ) {
                                            Text("Cancel")
                                        }
                                        Spacer(modifier = Modifier.width(20.dp))
                                        Button(
                                            onClick = {
                                                onEvent(AdminDetailsEvent.SubmitUpdatePassword)
                                            },
                                            enabled = !changePassword.isUpdatingPassword,
                                            colors = ButtonDefaults.buttonColors().copy(
                                                containerColor = RedE31,
                                                disabledContainerColor = RedBB0,
                                                contentColor = Color.White,
                                                disabledContentColor = Color.White
                                            ),
                                        ) {
                                            if(changePassword.isUpdatingPassword) {
                                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                                            } else {
                                                Text("Update")
                                            }
                                        }


                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {

                            Button(
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onEvent(AdminDetailsEvent.Logout)
                                }
                            ) {
                                Text("Logout", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                }
            }
        }

    }
}