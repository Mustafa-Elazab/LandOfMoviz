package com.example.landofmoviz.domain.usecase

import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.domain.repository.PersonRepository
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetail @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository,
    private val personRepository: PersonRepository
) {
    operator fun invoke(
        mediaType: MediaType,
        id: Int,
        seasonNumber: Int? = null,
        episodeNumber: Int? = null
    ): Flow<Resource<Any>> = flow {
        emit(
              when(mediaType){
                  MediaType.MOVIE -> movieRepository.getMovieDetails(movieId = id)
                  MediaType.TV -> if (seasonNumber != null) {
                      if (episodeNumber != null) tvRepository.getEpisodeDetails(id, seasonNumber, episodeNumber)
                      else tvRepository.getSeasonDetails(id, seasonNumber)
                  } else tvRepository.getTvDetails(id)
                  MediaType.PERSON -> personRepository.getPersonDetails(id)
              }
        )
    }
}