package com.example.moviego

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Checkbox
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import com.example.moviego.presentation.navgraph.NavGraph
import com.example.moviego.presentation.navgraph.Route
import com.example.moviego.presentation.user.payment_confirmation.UserPaymentConfirmationViewModel
import com.example.moviego.ui.theme.MovieGoTheme
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    val mainViewModel by viewModels<MainViewModel>()
    private val userPaymentConfirmationViewModel: UserPaymentConfirmationViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.splashCondition
            }
        }
        setContent {
            MovieGoTheme {
                val startDestination = mainViewModel.startDestination
                NavGraph(startDestination = startDestination, userPaymentConfirmationViewModel = userPaymentConfirmationViewModel)
            }
        }
    }

    override fun onPaymentSuccess(paymentId: String?, p1: PaymentData?) {
        Log.d("bookingId",paymentId!!)
        paymentId.let {
            userPaymentConfirmationViewModel.handlePaymentSuccess(paymentId!!)
        }

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        userPaymentConfirmationViewModel.handlePaymentError(p1.toString())
    }
}
