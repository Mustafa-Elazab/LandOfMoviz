package com.example.landofmoviz.domain.usecase


import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetByGenre @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) {
    operator fun invoke(mediaType: MediaType, genreId: Int, page: Int): Flow<Resource<Any>> = flow {
        emit(
            when (mediaType) {
                MediaType.MOVIE -> movieRepository.getMoviesByGenre(genreId, page)
                MediaType.TV -> tvRepository.getTvsByGenre(genreId, page)
                else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_MEDIA_TYPE)
            }
        )
    }
}