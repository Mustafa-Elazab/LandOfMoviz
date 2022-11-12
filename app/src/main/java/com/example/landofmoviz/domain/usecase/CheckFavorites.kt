package com.example.landofmoviz.domain.usecase


import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType

import javax.inject.Inject

class CheckFavorites @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    suspend operator fun invoke(
        mediaType: MediaType,
        id: Int
    ): Boolean = when (mediaType) {
        MediaType.MOVIE -> movieRepository.movieExists(id)
        MediaType.TV -> tvRepository.tvExists(id)
        else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
    }
}