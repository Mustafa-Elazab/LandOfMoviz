package com.example.landofmoviz.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.landofmoviz.data.local.entity.FavoriteMovieEntity
import com.example.landofmoviz.data.remote.dto.MovieDetailDTO
import com.example.landofmoviz.domain.model.FavoriteMovie
import com.example.landofmoviz.domain.model.MovieDetail
import com.example.landofmoviz.domain.model.MovieList
import com.example.landofmoviz.domain.model.VideoList
import com.example.landofmoviz.utils.Resource
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieRepository {
    suspend fun getMovieList(listId: String, page: Int, region: String?): Resource<MovieList>
    suspend fun getTrendingMovies(): Resource<MovieList>
    suspend fun getTrendingMovieTrailers(movieId: Int): Resource<VideoList>
    suspend fun getMoviesByGenre(genreId: Int, page: Int): Resource<MovieList>
    suspend fun getMovieSearchResults(query: String, page: Int): Resource<MovieList>
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetail>
    suspend fun getFavoriteMovies(): List<FavoriteMovie>
    suspend fun movieExists(movieId: Int): Boolean
    suspend fun insertMovie(movie: FavoriteMovie)
    suspend fun deleteMovie(movie: FavoriteMovie)
}