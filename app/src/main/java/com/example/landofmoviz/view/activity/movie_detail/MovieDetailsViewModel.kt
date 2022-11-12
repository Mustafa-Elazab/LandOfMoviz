package com.example.landofmoviz.view.activity.movie_detail

import androidx.lifecycle.viewModelScope
import com.example.landofmoviz.domain.model.FavoriteMovie
import com.example.landofmoviz.domain.model.MovieDetail
import com.example.landofmoviz.domain.usecase.AddFavorite
import com.example.landofmoviz.domain.usecase.CheckFavorites
import com.example.landofmoviz.domain.usecase.DeleteFavorite
import com.example.landofmoviz.domain.usecase.GetDetail
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import com.example.landofmoviz.utils.UiState
import com.example.landofmoviz.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getDetails: GetDetail,
    private val checkFavorites: CheckFavorites,
    private val deleteFavorite: DeleteFavorite,
    private val addFavorite: AddFavorite
) : BaseViewModel() {

    private val _details = MutableStateFlow(MovieDetail.empty)
    val details get() = _details.asStateFlow()

    private val _isInFavorites = MutableStateFlow(false)
    val isInFavorites get() = _isInFavorites.asStateFlow()

    private lateinit var favoriteMovie: FavoriteMovie

    private fun fetchMovieDetails() {
        viewModelScope.launch {
            getDetails(MediaType.MOVIE, id).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        (response.data as MovieDetail).apply {
                            _details.value = this
                            favoriteMovie = FavoriteMovie(
                                id = id,
                                posterPath = posterPath,
                                releaseDate = releaseDate,
                                runtime = runtime,
                                title = title,
                                voteAverage = voteAverage,
                                voteCount = voteCount,
                                date = System.currentTimeMillis()
                            )
                        }
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(errorText = response.message)
                    }
                }
            }
        }
    }

    private fun checkFavorites() {
        viewModelScope.launch {
            _isInFavorites.value = checkFavorites(MediaType.MOVIE, id)
        }
    }

    fun updateFavorites() {
        viewModelScope.launch {
            if (_isInFavorites.value) {
                deleteFavorite(mediaType = MediaType.MOVIE, movie = favoriteMovie)
                _isInFavorites.value = false
            } else {
                addFavorite(mediaType = MediaType.MOVIE, movie = favoriteMovie)
                _isInFavorites.value = true
            }
        }
    }

    fun initRequests(movieId: Int) {
        id = movieId
        checkFavorites()
        fetchMovieDetails()
    }
}