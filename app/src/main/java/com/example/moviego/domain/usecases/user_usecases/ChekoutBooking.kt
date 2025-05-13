package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.extractData

class CheckoutBooking(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(paymentId: String): Result<String> {
        return try {
            val response = userRepository.checkoutBooking(paymentId)
            if(response.isSuccessful){
                val message = extractData(response.body(), "message")
                return Result.success(message ?: "Success")
            } else {
                val message = extractData(response.errorBody(), "message")
                return Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}