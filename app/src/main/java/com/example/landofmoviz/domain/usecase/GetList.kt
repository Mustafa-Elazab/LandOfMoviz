package com.example.landofmoviz.domain.usecase

import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetList @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {

    operator fun invoke(
        mediaType: MediaType,
        listId: String?,
        page: Int? = null,
        region: String? = null
    ): Flow<Resource<Any>> = flow {

        emit(
            when (mediaType) {
                MediaType.MOVIE -> if (listId == null) movieRepository.getTrendingMovies() else movieRepository.getMovieList(
                    listId = listId,
                    page = page!!,
                    region = region
                )
                MediaType.TV -> if (listId == null) tvRepository.getTrendingTvs() else tvRepository.getTvList(
                    listId = listId,
                    page = page!!,

                    )
                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
            }

        )
    }
}