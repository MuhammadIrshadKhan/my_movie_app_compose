package com.example.moviapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviapp.R
import com.example.moviapp.data.remote_data.api.MovieApiInterface
import com.example.moviapp.presentation.detailsScreenItems.DetailsViewModel
import com.example.moviapp.utills.RatingBar

@Composable
fun DetailsScreen() {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApiInterface.IMAGE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApiInterface.IMAGE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 20.dp)
    ) {
        item {
            // Backdrop Image
            if (backDropImageState is AsyncImagePainter.State.Error) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = detailsState.movie?.title
                    )
                }
            } else if (backDropImageState is AsyncImagePainter.State.Success) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    painter = backDropImageState.painter,
                    contentDescription = detailsState.movie?.title,
                    contentScale = ContentScale.Crop
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            // Movie Details Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Poster Image
                if (posterImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title
                        )
                    }
                } else if (posterImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .width(160.dp)
                            .height(250.dp)
                            .clip(RoundedCornerShape(22.dp)),
                        painter = posterImageState.painter,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Movie Info
                detailsState.movie?.let { movie ->
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Title
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = movie.title,
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Rating
                        Row(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            RatingBar(
                                startModifier = Modifier.size(18.dp),
                                rating = movie.vote_average / 2
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 12.sp,
                                color = Color.LightGray,
                                text = (movie.vote_average / 2).toString().take(3),
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Language
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.language) + movie.original_language
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Release Date
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.release_date) + movie.release_date
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Votes
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = movie.vote_count.toString() + stringResource(R.string.votes)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        item {
            // Overview
            Text(
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 19.sp,
                text = "Overview",
                fontWeight = FontWeight.SemiBold
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        detailsState.movie?.let {
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 16.sp,
                    text = it.overview
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}