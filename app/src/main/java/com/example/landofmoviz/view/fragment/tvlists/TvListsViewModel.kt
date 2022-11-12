package com.example.landofmoviz.view.fragment.tvlists

import androidx.lifecycle.viewModelScope
import com.example.landofmoviz.domain.model.Tv
import com.example.landofmoviz.domain.model.TvList
import com.example.landofmoviz.domain.usecase.GetList
import com.example.landofmoviz.domain.usecase.GetTrendingVideos
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import com.example.landofmoviz.utils.UiState
import com.example.landofmoviz.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TvListsViewModel
@Inject constructor(
    private val getList: GetList,
    private val getTrendingVideos: GetTrendingVideos
) : BaseViewModel() {

    private val _trendingTvShows = MutableStateFlow(emptyList<Tv>())
    val trendingTvShows get() = _trendingTvShows.asStateFlow()


    private val _popularTvShows = MutableStateFlow(emptyList<Tv>())
    val popularTvShows get() = _popularTvShows.asStateFlow()

    private val _topRatedTvShows = MutableStateFlow(emptyList<Tv>())
    val topRatedTvShows get() = _topRatedTvShows.asStateFlow()

    private val _airingTvShows = MutableStateFlow(emptyList<Tv>())
    val airingTvShows get() = _airingTvShows.asStateFlow()

    private val _airingTime = MutableStateFlow(Constants.LIST_ID_AIRING_DAY)
    val airingTime get() = _airingTime.asStateFlow()

    private var pagePopular = 1
    private var pageTopRated = 1
    private var pageAiring = 1

    init {

        initRequest()
    }

     fun initRequest() {
        viewModelScope.launch {
            coroutineScope {
                fetchLists()
                fetchLists(Constants.LIST_ID_POPULAR)
                fetchLists(Constants.LIST_ID_TOP_RATED)
                fetchLists(Constants.LIST_ID_AIRING_DAY)
            }
            setUiState()
        }
    }

    private suspend fun fetchLists(listId: String? = null) {
        val page = when (listId) {
            Constants.LIST_ID_POPULAR -> pagePopular
            Constants.LIST_ID_TOP_RATED -> pageTopRated
            Constants.LIST_ID_AIRING_DAY, Constants.LIST_ID_AIRING_WEEK -> pageAiring
            else -> null
        }
        getList(mediaType = MediaType.TV, listId, page)
            .collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val tvList = (response.data as TvList).results

                        when (listId) {
                            Constants.LIST_ID_POPULAR -> _popularTvShows.value += tvList
                            Constants.LIST_ID_TOP_RATED -> _topRatedTvShows.value += tvList
                            Constants.LIST_ID_AIRING_DAY, Constants.LIST_ID_AIRING_WEEK -> _airingTvShows.value += tvList
                            else -> _trendingTvShows.value = tvList
                        }
                        areResponsesSuccessful.add(true)
                        isInitial = false
                    }
                    is Resource.Error -> {
                        errorText = response.message
                        areResponsesSuccessful.add(false)
                    }
                }
            }
    }

    fun onLoadMore(type: Any?) {

        _uiState.value = UiState.loadingState(isInitial)

        when (type as String) {
            Constants.LIST_ID_POPULAR -> pagePopular++
            Constants.LIST_ID_TOP_RATED -> pageTopRated++
            Constants.LIST_ID_AIRING_DAY, Constants.LIST_ID_AIRING_WEEK -> pageAiring++
        }
        viewModelScope.launch {
            coroutineScope {
                fetchLists(type)
                setUiState()
            }
        }


    }

    fun getTrendingTrailer(tvId: Int) = runBlocking {
        var videoKey = ""

        coroutineScope {
            getTrendingVideos(mediaType = MediaType.TV, tvId).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        videoKey = response.data.filterVideos(true).last().key
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(false, response.message)
                    }
                }

            }
        }
        videoKey
    }

    fun switchAiringTime(airTime: String) {
        _uiState.value = UiState.loadingState(isInitial)
        _airingTvShows.value = emptyList()

        _airingTime.value = airTime
        pageAiring = 1

        viewModelScope.launch {
            coroutineScope {
                fetchLists(airTime)
                setUiState()
            }
        }

    }

}