package com.example.moviapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviapp.domain.model.Movie
import com.example.moviapp.domain.repository.MovieListRepository
import com.example.moviapp.utills.Category
import com.example.moviapp.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel(){

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(events: MovieListUiEvents){
        when(events){
            MovieListUiEvents.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }
            }
            is MovieListUiEvents.Paginate -> {
                if (events.category == Category.Popular){
                    getPopularMovieList(true)
                }else if(events.category == Category.UpComing){
                    getUpcomingMovieList(true)
                }
            }
        }

    }

    private fun getUpcomingMovieList(forceFetchFromRemote : Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UpComing,
                movieListState.value.upcomingMovieListPag
            ).collectLatest {result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }

                    }
                    is Resource.Success -> {
                        result.data?.let {upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList
                                            +upcomingList.shuffled(),
                                    upcomingMovieListPag = movieListState.value.upcomingMovieListPag +1
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }
                }
            }
        }
    }

    private fun getPopularMovieList(forceFetchFromRemote : Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.Popular,
                movieListState.value.popularMovieListPag
            ).collectLatest {result ->
                when(result){
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }

                    }
                    is Resource.Success -> {
                        result.data?.let {popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                    +popularList.shuffled(),
                                    popularMovieListPag = movieListState.value.popularMovieListPag +1,
                                    isLoading = false

                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }
                }
            }
        }
    }
}