package com.example.moviapp.presentation.detailsScreenItems

import com.example.moviapp.domain.model.Movie

data class DetailsItemState(
    val isLoading : Boolean = false,
    val movie: Movie? = null
)