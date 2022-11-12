package com.example.landofmoviz.domain.usecase

import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.PersonRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchResults @Inject constructor(
    private val movieRepository: MovieRepository, private val tvRepository: TvRepository,
    private val personRepository: PersonRepository
) {


    operator fun invoke(mediaType: MediaType, query: String, page: Int): Flow<Resource<Any>> =
        flow {

            emit(
                when (mediaType) {
                    MediaType.MOVIE -> movieRepository.getMovieSearchResults(query, page)
                    MediaType.TV -> tvRepository.getTvSearchResults(query, page)
                    MediaType.PERSON -> personRepository.getPersonSearchResults(query, page)
                }
            )
        }

}