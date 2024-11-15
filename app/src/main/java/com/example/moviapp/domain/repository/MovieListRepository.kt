package com.example.moviapp.domain.repository

import com.example.moviapp.domain.model.Movie
import com.example.moviapp.utills.Resource
import kotlinx.coroutines.flow.Flow


interface MovieListRepository {

    suspend fun getMovieList(
        forceFetchFromRemote : Boolean,
        category : String,
        page : Int
    ) : Flow<Resource<List<Movie>>>

    suspend fun getMovie(id : Int) : Flow<Resource<Movie>>
}