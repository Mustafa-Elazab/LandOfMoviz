package com.example.landofmoviz.domain.usecase

import com.example.landofmoviz.domain.model.VideoList
import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTrendingVideos @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {

    operator fun invoke(mediaType: MediaType, id: Int): Flow<Resource<VideoList>> = flow {
        emit(
            when (mediaType) {
                MediaType.MOVIE -> movieRepository.getTrendingMovieTrailers(movieId = id)
                MediaType.TV -> tvRepository.getTrendingTvTrailers(tvId = id)
                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
            }
        )
    }
}