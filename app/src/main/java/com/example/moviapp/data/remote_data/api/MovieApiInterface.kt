package com.example.moviapp.data.remote_data.api

import com.example.moviapp.data.remote_data.reponse_model.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {
    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category : String,
        @Query("page") page : Int,
        @Query("api_key") apikey : String = API_KEY
    ) : MovieListDto

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "dd777bf1bbd2a3b4f2bc32ee4605966f"
    }

}