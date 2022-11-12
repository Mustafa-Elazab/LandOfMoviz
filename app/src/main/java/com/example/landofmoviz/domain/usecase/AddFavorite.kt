package com.example.landofmoviz.domain.usecase


import com.example.landofmoviz.domain.model.FavoriteMovie
import com.example.landofmoviz.domain.model.FavoriteTv
import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType


import javax.inject.Inject

class AddFavorite @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        mediaType: MediaType,
        movie: FavoriteMovie? = null,
        tv: FavoriteTv? = null
    ) {
        when (mediaType) {
            MediaType.MOVIE -> movieRepository.insertMovie(movie!!)
            MediaType.TV -> tvRepository.insertTv(tv!!)
            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
        }
    }
}