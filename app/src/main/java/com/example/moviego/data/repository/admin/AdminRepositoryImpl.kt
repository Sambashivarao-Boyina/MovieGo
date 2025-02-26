package com.example.moviego.data.repository.admin

import com.example.moviego.data.remote.Dao.AuthResponse
import com.example.moviego.data.remote.Dao.Login
import com.example.moviego.data.remote.Dao.NewShow
import com.example.moviego.data.remote.Dao.SignUp
import com.example.moviego.data.remote.MovieGoApi
import com.example.moviego.domain.model.Movie
import com.example.moviego.domain.model.Show
import com.example.moviego.domain.model.ShowDetails
import com.example.moviego.domain.model.TheaterDetails
import com.example.moviego.domain.repository.admin.AdminRepository
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

    override suspend fun getAllAdminTheaters(): Response<List<TheaterDetails>> {
        return movieGoApi.getAllAdminTheaters()
    }

    override suspend fun createNewShow(newShow: NewShow): Response<ResponseBody> {
        return movieGoApi.createShow(newShow)
    }


}