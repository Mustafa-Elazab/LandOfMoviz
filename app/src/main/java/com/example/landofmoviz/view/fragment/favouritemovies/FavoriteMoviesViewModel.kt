package com.example.landofmoviz.view.fragment.favouritemovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landofmoviz.domain.model.FavoriteMovie
import com.example.landofmoviz.domain.usecase.AddFavorite
import com.example.landofmoviz.domain.usecase.DeleteFavorite
import com.example.landofmoviz.domain.usecase.GetFavourite
import com.example.landofmoviz.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(
    private val addFavorite: AddFavorite,
    private val getFavourite: GetFavourite,
    private val deleteFavorite: DeleteFavorite
) : ViewModel() {

    private val _favoriteMovies = MutableStateFlow(emptyList<FavoriteMovie>())
    val favoriteMovies get() = _favoriteMovies.asStateFlow()

     fun fetchFavoriteMovies() {
        viewModelScope.launch {
            getFavourite(mediaType = MediaType.MOVIE).collect {
                _favoriteMovies.value = it as List<FavoriteMovie>
            }
        }
    }

    fun removeMovieFromFavorites(movie: FavoriteMovie) {
        viewModelScope.launch {
            deleteFavorite(mediaType = MediaType.MOVIE, movie)
            fetchFavoriteMovies()
        }
    }

    fun addMovieToFavorites(movie: FavoriteMovie) {
        viewModelScope.launch {
            addFavorite(mediaType = MediaType.MOVIE, movie)
            fetchFavoriteMovies()
        }
    }

    init {
        fetchFavoriteMovies()
    }
}