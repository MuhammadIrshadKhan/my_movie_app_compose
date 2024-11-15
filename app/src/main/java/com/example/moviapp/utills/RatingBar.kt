package com.example.moviapp.utills

import android.media.Rating
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.moviapp.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    startModifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starColor: Color = Color.Yellow
) {
    val filledStars = kotlin.math.floor(rating).toInt()
    val unfilledStars = (stars - kotlin.math.ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))

    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(
                modifier = startModifier,
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                tint = starColor
            )
        }
        if (halfStar) {
            Icon(
                modifier = startModifier,
                painter = painterResource(id = R.drawable.half_star),
                contentDescription = null,
                tint = starColor
            )
        }
        repeat(unfilledStars) {
            Icon(
                modifier = startModifier,
                painter = painterResource(id = R.drawable.star_outline),
                contentDescription = null,
                tint = starColor
            )
        }

    }
}