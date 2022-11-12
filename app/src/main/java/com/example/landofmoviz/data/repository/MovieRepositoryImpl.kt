package com.example.landofmoviz.data.repository

import com.example.landofmoviz.data.local.dao.MovieDao
import com.example.landofmoviz.data.mapper.*
import com.example.landofmoviz.data.remote.api.MovieApi
import com.example.landofmoviz.domain.model.FavoriteMovie
import com.example.landofmoviz.domain.model.MovieDetail
import com.example.landofmoviz.domain.model.MovieList
import com.example.landofmoviz.domain.model.VideoList
import com.example.landofmoviz.domain.repository.MovieRepository
import com.example.landofmoviz.utils.Resource
import com.example.landofmoviz.utils.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val safeApiCall: SafeApiCall
) : MovieRepository {

    override suspend fun getMovieList(
        listId: String,
        page: Int,
        region: String?
    ): Resource<MovieList> = safeApiCall.execute {
        api.getMovieList(listId, page, region).toMovieList()
    }

    override suspend fun getTrendingMovies(): Resource<MovieList> = safeApiCall.execute {
        api.getTrendingMovies().toMovieList()
    }

    override suspend fun getTrendingMovieTrailers(movieId: Int): Resource<VideoList> =
        safeApiCall.execute {
            api.getTrendingMovieTrailers(movieId).toVideoList()
        }

    override suspend fun getMoviesByGenre(genreId: Int, page: Int): Resource<MovieList> =
        safeApiCall.execute {
            api.getMoviesByGenre(genreId, page).toMovieList()
        }

    override suspend fun getMovieSearchResults(query: String, page: Int): Resource<MovieList> =
        safeApiCall.execute {
            api.getMovieSearchResults(query, page).toMovieList()
        }

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail> =
        safeApiCall.execute {
            api.getMovieDetails(movieId).toMovieDetail()
        }

    override suspend fun getFavoriteMovies(): List<FavoriteMovie> =
        dao.getAllMovies().map { it.toFavoriteMovie() }

    override suspend fun movieExists(movieId: Int): Boolean = dao.movieExists(movieId)

    override suspend fun insertMovie(movie: FavoriteMovie) =  dao.insertMovie(movie.toFavoriteMovieEntity())

    override suspend fun deleteMovie(movie: FavoriteMovie) = dao.deleteMovie(movie.toFavoriteMovieEntity())

}