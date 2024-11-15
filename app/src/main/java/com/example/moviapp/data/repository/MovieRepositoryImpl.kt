package com.example.moviapp.data.repository

import com.example.moviapp.data.local_data.MovieDatabase
import com.example.moviapp.data.mappers.toMovie
import com.example.moviapp.data.mappers.toMovieEntity
import com.example.moviapp.data.remote_data.api.MovieApiInterface
import com.example.moviapp.domain.model.Movie
import com.example.moviapp.domain.repository.MovieListRepository
import com.example.moviapp.utills.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    val movieApiInterface: MovieApiInterface,
    val database: MovieDatabase
) : MovieListRepository{
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = database.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi =
            try {
                movieApiInterface.getMovieList(category,page)
            }catch (e : IOException){
                e.printStackTrace()
                emit(Resource.Error(message = "Failed to load data"))
                return@flow
            }catch (e : HttpException){
                e.printStackTrace()
                emit(Resource.Error(message = "Failed to load data"))
                return@flow
            }catch (e : Exception){
                e.printStackTrace()
                emit(Resource.Error(message = "Failed to load data"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            database.movieDao.upsetMovieList(movieEntities)

            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = database.movieDao.getMovieById(id)
            if(movieEntity != null){
                emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow

            }
            emit(Resource.Error(message = "failed to load data"))
            emit(Resource.Loading(false))
        }
    }
}