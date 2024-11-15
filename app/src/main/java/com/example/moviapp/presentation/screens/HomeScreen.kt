package com.example.moviapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviapp.presentation.MovieListUiEvents
import com.example.moviapp.presentation.MovieListViewModel
import com.example.moviapp.utills.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = viewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text =
                    if (movieListState.isCurrentPopularScreen) "Popular Movies"
                    else "UpComing Movies",
                    fontSize = 20.sp
                )
                //modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top)

            },
            modifier = Modifier.shadow(2.dp)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                MaterialTheme.colorScheme.inverseOnSurface
            )
        )
    },
        bottomBar = {
            bottomNavigation(
                bottomNavController = bottomNavController,
                onEvent = viewModel::onEvent
            )
        },
        modifier = Modifier.windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Top)),

    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            NavHost(navController = bottomNavController,
                    startDestination =  Screens.PopularScreen.route) {
                composable(Screens.PopularScreen.route){
                     PopularMoviesScreen(
                         navController = navController,
                         movieListState = movieListState,
                         onEvent = viewModel::onEvent
                     )
                }
                composable(Screens.UpComingScreen.route){
                      UpcomingMoviesScreen(
                          navController = navController,
                          movieListState = movieListState,
                          onEvent = viewModel::onEvent
                      )
                }
            }
        }

    }

}

@Composable
fun bottomNavigation(
    bottomNavController: NavHostController,
    onEvent: (MovieListUiEvents) -> Unit
) {
    val items = listOf(
        BottomItems(
            title = "Popular",
            image = Icons.Rounded.Movie
        ),
        BottomItems(
            title = "Upcoming",
            image = Icons.Rounded.Upcoming
        )

    )

    val selected = rememberSaveable() {
        mutableIntStateOf(0)

    }
    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItems ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                onEvent(MovieListUiEvents.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screens.PopularScreen.route)
                            }

                            1 -> {
                                onEvent(MovieListUiEvents.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screens.UpComingScreen.route)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItems.image,
                            contentDescription = bottomItems.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    label = {
                        Text(
                            text = bottomItems.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    })
            }

        }
    }

}

data class BottomItems(
    val title: String,
    val image: ImageVector
)
