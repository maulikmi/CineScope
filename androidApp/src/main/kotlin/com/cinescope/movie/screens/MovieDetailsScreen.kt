package com.cinescope.movie.screens

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.cinescope.movie.R
import com.cinescope.movie.domain.movie.details.MovieDetails
import com.cinescope.movie.domain.movie.details.MovieDetailsStateHolder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cinescope.movie.TmdbConstants
import com.cinescope.movie.TmdbImageUrlBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    stateHolder: MovieDetailsStateHolder,
    onBack: () -> Unit
) {
    val uiState by stateHolder.uiState.collectAsState()

    LaunchedEffect(movieId) {
        stateHolder.loadMovieDetails(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Movie Details")
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            modifier = Modifier.size(25.dp),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        when {
            uiState.isLoading && uiState.movie == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding()),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator()

                        Text(
                            text = "Loading movie..."
                        )
                    }
                }
            }

            uiState.error != null && uiState.movie == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            style = MaterialTheme.typography.displaySmall
                        )

                        Text(
                            text = uiState.error
                                ?: "Something went wrong",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Button(
                            onClick = {
                                stateHolder.loadMovieDetails(movieId)
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            uiState.movie != null -> {
                uiState.movie?.let { movie ->
                    MovieDetailsContent(
                        movie = movie,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: MovieDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {

        // Backdrop
        movie.backdropPath?.let { backdropPath ->
            AsyncImage(
                model = TmdbImageUrlBuilder.backdrop(backdropPath),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Poster + basic information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {

                movie.posterPath?.let { posterPath ->
                    AsyncImage(
                        model = "$TmdbConstants.IMAGE_BASE_URL/w500$posterPath",
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    movie.releaseDate
                        ?.takeIf { it.isNotBlank() }
                        ?.let { releaseDate ->
                            Text(
                                text = releaseDate,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFF5C518),
                            contentDescription = "Rating"
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text = String.format(
                                java.util.Locale.US,
                                "%.1f",
                                movie.rating
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    movie.runtime?.let { runtime ->
                        Text(
                            text = formatRuntime(runtime),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Genres
            if (movie.genres.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movie.genres.forEach { genre ->
                        Text(
                            text = genre.name,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    MaterialTheme.colorScheme
                                        .secondaryContainer
                                )
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                        )
                    }
                }
            }

            // Overview
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = movie.overview
                    ?.takeIf { it.isNotBlank() }
                    ?: "No overview available.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Vote count
            Text(
                text = "${movie.voteCount} votes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatRuntime(
    runtimeMinutes: Int
): String {
    val hours = runtimeMinutes / 60
    val minutes = runtimeMinutes % 60

    return when {
        hours > 0 && minutes > 0 ->
            "${hours}h ${minutes}m"

        hours > 0 ->
            "${hours}h"

        else ->
            "${minutes}m"
    }
}