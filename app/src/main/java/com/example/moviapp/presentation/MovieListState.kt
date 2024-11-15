package com.example.moviapp.presentation

import com.example.moviapp.domain.model.Movie

data class MovieListState(
    val isLoading : Boolean = false,

    val popularMovieListPag : Int = 1,
    val upcomingMovieListPag : Int = 1,

    val isCurrentPopularScreen : Boolean = true,

    val popularMovieList : List<Movie> = emptyList(),
    val upcomingMovieList : List<Movie> = emptyList()
)
