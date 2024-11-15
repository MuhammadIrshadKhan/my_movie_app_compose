package com.example.moviapp.di

import com.example.moviapp.data.repository.MovieRepositoryImpl
import com.example.moviapp.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieLisMovieRepositoryImpl: MovieRepositoryImpl
    ) : MovieListRepository
}