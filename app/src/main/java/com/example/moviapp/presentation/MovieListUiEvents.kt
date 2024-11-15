package com.example.moviapp.presentation

sealed interface MovieListUiEvents {
    data class Paginate(val category : String) : MovieListUiEvents
    object Navigate : MovieListUiEvents
}