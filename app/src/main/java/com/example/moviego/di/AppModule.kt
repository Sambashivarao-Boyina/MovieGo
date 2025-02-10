package com.example.moviego.di

import android.app.Application
import com.example.moviego.data.manager.LocalUserManagerImpl
import com.example.moviego.data.remote.MovieGoApi
import com.example.moviego.data.repository.admin.AdminRepositoryImp
import com.example.moviego.data.repository.user.UserRepositoryImpl
import com.example.moviego.domain.manager.LocalUserManager
import com.example.moviego.domain.repository.admin.AdminRepository
import com.example.moviego.domain.repository.user.UserRepository
import com.example.moviego.domain.usecases.admin_usecases.AdminUseCases
import com.example.moviego.domain.usecases.admin_usecases.LoginAdminUseCase
import com.example.moviego.domain.usecases.admin_usecases.RefreshAdminToken
import com.example.moviego.domain.usecases.admin_usecases.SignUpAdminUseCase
import com.example.moviego.domain.usecases.user_usecases.LoginUserUseCase
import com.example.moviego.domain.usecases.user_usecases.RefreshUserToken
import com.example.moviego.domain.usecases.user_usecases.SignUpUserUseCase
import com.example.moviego.domain.usecases.user_usecases.UserUseCases
import com.example.moviego.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ):LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideMovieGoApi(
        localUserManager: LocalUserManager
    ): MovieGoApi {
        val TIMEOUT = 30L
        val CONNECTION_TIMEOUT = 15L

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor{ chain ->
                    val token = runBlocking {
                        localUserManager.getUserToken().firstOrNull() ?: ""
                    }
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header(
                            "Authorization",
                            "Bearer ${token}"
                        )
                        .header("Content-Type", "application/json")
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()
        }

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieGoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        movieGoApi: MovieGoApi
    ):AdminRepository = AdminRepositoryImp(movieGoApi = movieGoApi)

    @Provides
    @Singleton
    fun provideUserRepository(
        movieGoApi: MovieGoApi
    ): UserRepository = UserRepositoryImpl(movieGoApi = movieGoApi)

    @Provides
    @Singleton
    fun provideAdminUseCases(
        adminRepository: AdminRepository,
        localUserManager: LocalUserManager
    ):AdminUseCases{
        return  AdminUseCases(
            loginAdminUseCase = LoginAdminUseCase(adminRepository, localUserManager),
            signUpAdminUseCase = SignUpAdminUseCase(adminRepository, localUserManager),
            refreshAdminToken = RefreshAdminToken(adminRepository,localUserManager)
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepository: UserRepository,
        localUserManager: LocalUserManager
    ):UserUseCases {
        return UserUseCases(
            loginUserUseCase = LoginUserUseCase(userRepository,localUserManager),
            signUpUserUseCase = SignUpUserUseCase(userRepository,localUserManager),
            refreshUserToken = RefreshUserToken(userRepository, localUserManager)
        )
    }
}