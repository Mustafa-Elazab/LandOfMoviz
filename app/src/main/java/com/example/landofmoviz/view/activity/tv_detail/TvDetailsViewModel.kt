package com.example.landofmoviz.view.activity.tv_detail

import androidx.lifecycle.viewModelScope
import com.example.landofmoviz.domain.model.FavoriteTv
import com.example.landofmoviz.domain.model.TvDetail
import com.example.landofmoviz.domain.usecase.AddFavorite
import com.example.landofmoviz.domain.usecase.CheckFavorites
import com.example.landofmoviz.domain.usecase.DeleteFavorite
import com.example.landofmoviz.domain.usecase.GetDetail
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.utils.Resource
import com.example.landofmoviz.utils.UiState
import com.example.landofmoviz.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailsViewModel @Inject constructor(
    private val getDetails: GetDetail,
    private val checkFavorites: CheckFavorites,
    private val deleteFavorite: DeleteFavorite,
    private val addFavorite: AddFavorite
) : BaseViewModel() {

    private val _details = MutableStateFlow(TvDetail.empty)
    val details get() = _details.asStateFlow()

    private val _isInFavorites = MutableStateFlow(false)
    val isInFavorites get() = _isInFavorites.asStateFlow()

    private lateinit var favoriteTv: FavoriteTv

    private fun fetchTvDetails() {
        viewModelScope.launch {

            getDetails(mediaType = MediaType.TV, id = id).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        (response.data as TvDetail).apply {
                            _details.value = this
                            favoriteTv = FavoriteTv(
                                id = id,
                                episodeRunTime = if (episodeRunTime.isEmpty()) 0 else episodeRunTime[0],
                                firstAirDate = firstAirDate,
                                name = name,
                                posterPath = posterPath,
                                voteAverage = voteAverage,
                                voteCount = voteCount,
                                date = System.currentTimeMillis()
                            )
                        }
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState(false, response.message)
                    }
                }

            }


        }
    }


    fun checkFavorites() {
        viewModelScope.launch {
            _isInFavorites.value = checkFavorites(mediaType = MediaType.TV, id)
        }
    }

    fun updateFavorites() {
        viewModelScope.launch {
            if (_isInFavorites.value) {
                deleteFavorite(mediaType = MediaType.TV, tv = favoriteTv)
                _isInFavorites.value = false
            } else {
                addFavorite(mediaType = MediaType.TV, tv = favoriteTv)
                _isInFavorites.value = true
            }
        }
    }

    fun initRequest(tvId: Int) {
        id = tvId
        checkFavorites()
        fetchTvDetails()
    }

}