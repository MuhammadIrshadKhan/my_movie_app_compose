package com.example.moviapp.utills


sealed class Screens(val route: String) {
    object HomeScreen : Screens("home_screen")
    object PopularScreen : Screens("popular_screen")
    object UpComingScreen : Screens("upcoming_screen")
    object DetailsScreen : Screens("details_screen")
}