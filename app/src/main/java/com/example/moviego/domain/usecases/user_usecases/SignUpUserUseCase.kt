package com.example.moviego.domain.usecases.user_usecases

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.util.Constants.USER
import com.example.moviego.util.Constants.extractData

class SignUpUserUseCase(
    private val userRepository: UserRepository,
    private val localUserManager: LocalUserManager
) {
    suspend operator fun invoke(user: SignUp) : Result<String> {
        return try {
            val response = userRepository.signUpUser(user)
            if(response.isSuccessful) {
                response.body()?.let { authResponse: AuthResponse ->
                    if(authResponse.token.isNotEmpty()){
                        localUserManager.saveToken(authResponse.token)
                        localUserManager.saveUserType(USER)
                        return Result.success(authResponse.message)
                    }

                }
                return Result.failure(Exception("Some thing went wrong"))
            } else {
                val message = extractData(response.errorBody(),"message")
                return Result.failure(Exception(message))
            }
        }catch (e:Exception) {
            return Result.failure(e)
        }
    }
}