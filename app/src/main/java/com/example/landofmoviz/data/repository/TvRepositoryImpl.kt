package com.example.landofmoviz.data.repository

import com.example.landofmoviz.data.local.dao.TvDao
import com.example.landofmoviz.data.mapper.*
import com.example.landofmoviz.data.remote.api.TvApi
import com.example.landofmoviz.domain.model.*
import com.example.landofmoviz.domain.repository.TvRepository
import com.example.landofmoviz.utils.Resource
import com.example.landofmoviz.utils.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvRepositoryImpl @Inject constructor(
    private val api: TvApi,
    private val dao: TvDao,
    private val safeApiCall: SafeApiCall
) : TvRepository {
    override suspend fun getTvList(listId: String, page: Int): Resource<TvList> =
        safeApiCall.execute {
            api.getTvList(listId, page).toTvList()
        }

    override suspend fun getTrendingTvs(): Resource<TvList> = safeApiCall.execute {
        api.getTrendingTvs().toTvList()
    }

    override suspend fun getTrendingTvTrailers(tvId: Int): Resource<VideoList> =
        safeApiCall.execute {
            api.getTrendingTvTrailers(tvId).toVideoList()
        }

    override suspend fun getTvsByGenre(genreId: Int, page: Int): Resource<TvList> =
        safeApiCall.execute {
            api.getTvsByGenre(genreId, page).toTvList()
        }

    override suspend fun getTvSearchResults(query: String, page: Int): Resource<TvList> =
        safeApiCall.execute {
            api.getTvSearchResults(query, page).toTvList()
        }

    override suspend fun getTvDetails(tvId: Int): Resource<TvDetail> = safeApiCall.execute {
        api.getTvDetails(tvId).toTvDetail()
    }

    override suspend fun getSeasonDetails(tvId: Int, seasonNumber: Int): Resource<SeasonDetail> =
        safeApiCall.execute {
            api.getSeasonDetails(tvId, seasonNumber).toSeasonDetail()
        }

    override suspend fun getEpisodeDetails(
        tvId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Resource<EpisodeDetail> = safeApiCall.execute {
        api.getEpisodeDetails(tvId, seasonNumber, episodeNumber)
            .toEpisodeDetail()
    }

    override suspend fun getFavoriteTvs(): List<FavoriteTv> =
        dao.getAllTvs().map { it.toFavoriteTv() }

    override suspend fun tvExists(tvId: Int): Boolean = dao.tvExists(tvId)

    override suspend fun insertTv(tv: FavoriteTv) = dao.insertTv(tv.toFavoriteTvEntity())

    override suspend fun deleteTv(tv: FavoriteTv) = dao.deleteTv(tv.toFavoriteTvEntity())
}