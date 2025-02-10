package com.example.moviego.presentation.authentication.adminAuth.admin_signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviego.R
import com.example.moviego.presentation.authentication.components.InputBox
import com.example.moviego.presentation.authentication.components.PasswordInput
import com.example.moviego.presentation.authentication.components.SubmitButton
import com.example.moviego.ui.theme.RedE31

@Composable
fun AdminSignUpScreen(
    state: AdminSignUpState,
    onEvent:(AdminSignUpEvent) -> Unit,
    isLoading: Boolean,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.moviego),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        Text(
            text = buildAnnotatedString {
                append("Welcome to ")
                withStyle(style = SpanStyle(color = RedE31, fontWeight = FontWeight.Bold)) {
                    append("MovieGo")
                }
            },
            style = MaterialTheme.typography.headlineLarge
        )

        InputBox(
            value = state.email,
            onChange = {
                onEvent(AdminSignUpEvent.UpdateEmail(it))
            },
            placeHolder = "Enter Email",
            leadingIcon = {
                Icon(painter = painterResource(R.drawable.email), contentDescription = null)
            },
            error = state.emailError,
            keyboardType = KeyboardType.Text
        )

        InputBox(
            value = state.name,
            onChange = {
                onEvent(AdminSignUpEvent.UpdateName(it))
            },
            placeHolder = "Enter UserName",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            error = state.nameError,
            keyboardType = KeyboardType.Text
        )

        InputBox(
            value = state.phone,
            onChange = {
                onEvent(AdminSignUpEvent.UpdatePhone(it))
            },
            placeHolder = "Enter Phone Number",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null)
            },
            error = state.phoneError,
            keyboardType = KeyboardType.Phone
        )
        PasswordInput(
            value = state.password,
            onChange = {
                onEvent(AdminSignUpEvent.UpdatePassword(it))
            },
            placeholder = "Enter Password",
            error = state.passwordError
        )
        PasswordInput(
            value = state.confirmPassword,
            onChange = {
                onEvent(AdminSignUpEvent.UpdateConfirmPassword(it))
            },
            placeholder = "Enter Confirm Password",
            error = state.confirmPasswordError
        )

        SubmitButton(
            onClick = {
                onEvent(AdminSignUpEvent.SignUp)
            },
            title = "Sign Up",
            loading = isLoading
        )

        Text(
            text = buildAnnotatedString {
                append("Already Registered ?")
                withStyle(style = SpanStyle(color = RedE31)) {
                    append(" Login")
                }
            },
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )

    }
}