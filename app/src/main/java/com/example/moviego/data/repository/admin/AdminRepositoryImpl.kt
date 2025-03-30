package com.example.moviego.data.repository.admin

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.NewScreen
import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.Dao.UpdateBody
import com.example.moviego.data.remote.MovieGoApi
import com.example.moviego.domain.model.Admin
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.repository.admin.AdminRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class AdminRepositoryImp(
    private val movieGoApi: MovieGoApi
) : AdminRepository {
    override suspend fun loginAdmin(admin: Login): Response<AuthResponse> {
        return movieGoApi.loginAdmin(admin)
    }

    override suspend fun signUpAdmin(admin: SignUp): Response<AuthResponse> {
        return movieGoApi.signUpAdmin(admin)
    }

    override suspend fun refreshAdminToken(): Response<AuthResponse> {
        return movieGoApi.refreshAdminToken()
    }

    override suspend fun getAllShows(): Response<List<Show>> {
        return movieGoApi.getAllShows()
    }

    override suspend fun getShowDetails(showId: String): Response<ShowDetails> {
        return movieGoApi.getShowDetails(showId)
    }

    override suspend fun getAllAdminMovies(): Response<List<Movie>> {
        return movieGoApi.getAllAdminMovies()
    }

    override suspend fun getMovieDetails(movieId: String): Response<Movie> {
        return movieGoApi.getMovieDetails(movieId)
    }

    override suspend fun getAllAdminTheaters(): Response<List<TheaterDetails>> {
        return movieGoApi.getAllAdminTheaters()
    }

    override suspend fun createNewShow(newShow: NewShow): Response<ResponseBody> {
        return movieGoApi.createShow(newShow)
    }

    override suspend fun getAdminDetails(): Response<Admin> {
        return movieGoApi.getAdminDetails()
    }

    override suspend fun updatePhoneNumber(data: UpdateBody): Response<Admin> {
        return movieGoApi.updatePhone(data = data)
    }

    override suspend fun updatePassword(data: UpdateBody): Response<ResponseBody> {
        return movieGoApi.updatePassword(data)
    }

    override suspend fun getTheaterDetails(theaterId: String): Response<TheaterDetails> {
        return movieGoApi.getTheaterDetails(theaterId)
    }

    override suspend fun addNewMovie(
        movieName: String
    ): Response<ResponseBody> {
        return movieGoApi.addNewMovie(movieName)
    }

    override suspend fun createNewScreen(
        theaterId: String,
        newScreen: NewScreen
    ): Response<TheaterDetails> {
        return  movieGoApi.createNewScreen(theaterId = theaterId, newScreen = newScreen)
    }

    override suspend fun editScreen(
        screenId: String,
        editScreen: NewScreen
    ): Response<TheaterDetails> {
        return movieGoApi.editScreen(screenId = screenId, editScreen = editScreen)
    }

    override suspend fun addNewTheater(
        image: MultipartBody.Part,
        newTheater: RequestBody
    ): Response<ResponseBody> {
        return movieGoApi.addNewTheater(image = image, theater = newTheater)
    }

    override suspend fun editTheater(
        theaterId: String,
        image: MultipartBody.Part?,
        editTheater: RequestBody
    ): Response<ResponseBody> {
        return movieGoApi.editTheater(image = image, theater = editTheater, theaterId = theaterId)
    }
}