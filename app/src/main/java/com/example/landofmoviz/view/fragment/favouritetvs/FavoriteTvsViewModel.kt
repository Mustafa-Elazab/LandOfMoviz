package com.example.landofmoviz.view.fragment.favouritetvs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.landofmoviz.domain.model.FavoriteTv
import com.example.landofmoviz.domain.usecase.AddFavorite
import com.example.landofmoviz.domain.usecase.DeleteFavorite
import com.example.landofmoviz.domain.usecase.GetFavourite
import com.example.landofmoviz.utils.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteTvsViewModel @Inject constructor(
    private val addFavorite: AddFavorite,
    private val deleteFavorite: DeleteFavorite,
    private val getFavourite: GetFavourite
) : ViewModel() {

    private val _favoriteTvs = MutableStateFlow(emptyList<FavoriteTv>())
    val favoriteTvs get() = _favoriteTvs.asStateFlow()


    init {

        fetchFavoriteTvs()
    }

    fun fetchFavoriteTvs() {
        viewModelScope.launch {
            getFavourite(mediaType = MediaType.TV).collect {
                _favoriteTvs.value = it as List<FavoriteTv>
            }
        }
    }

    fun removeTvFromFavorites(tv: FavoriteTv) {
        viewModelScope.launch {
            deleteFavorite(mediaType = MediaType.TV, tv = tv)
            fetchFavoriteTvs()
        }
    }


    fun addTvToFavorites(tv: FavoriteTv) {
        viewModelScope.launch {
            addFavorite(mediaType = MediaType.TV, tv = tv)
            fetchFavoriteTvs()
        }
    }

    init {
        fetchFavoriteTvs()
    }

}