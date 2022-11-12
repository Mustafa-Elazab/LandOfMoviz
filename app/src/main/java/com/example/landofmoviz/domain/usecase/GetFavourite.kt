package com.example.landofmoviz.domain.usecase

import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavourite @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {


    operator fun invoke(
        mediaType: MediaType,
    ): Flow<List<Any>> = flow {
        emit(
            when (mediaType) {
                MediaType.MOVIE -> movieRepository.getFavoriteMovies()
                MediaType.TV -> tvRepository.getFavoriteTvs()
                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)

            }

        )
    }
}