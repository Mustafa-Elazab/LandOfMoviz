package com.example.landofmoviz.view.activity.tv_detail

import android.os.Bundle
import androidx.activity.viewModels
import com.example.landofmoviz.R
import com.example.landofmoviz.databinding.ActivityTvDetailsBinding
import com.example.landofmoviz.view.activity.BaseActivity
import com.example.landofmoviz.view.adapter.ImageAdapter
import com.example.landofmoviz.view.adapter.PersonAdapter
import com.example.landofmoviz.view.adapter.TvAdapter
import com.example.landofmoviz.view.adapter.VideoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvDetailsActivity : BaseActivity<ActivityTvDetailsBinding>(R.layout.activity_tv_details) {

    private val viewModel: TvDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        viewModel.initRequest(id)
        collectFlows(listOf(::collectDetails, ::collectUiState))
    }

    private suspend fun collectDetails() {
        viewModel.details.collect { details ->
            adapterCast.submitList(details.credits.cast)
            adapterVideos.submitList(details.videos.filterVideos())
            adapterImages.submitList(details.images.backdrops)
         //   adapterSeasons.submitList(details.seasons)
            adapterRecommendations.submitList(details.recommendations.results)
        }
    }

    private suspend fun collectUiState(){
        viewModel.uiState.collect{ state ->
          if (state.isError)  showSnackbar(state.errorText!!, getString(R.string.button_retry)){
              viewModel.retryConnection {
                  viewModel.initRequest(id)
              }
          }
        }
    }

    override val defineBindingVariables: ((ActivityTvDetailsBinding) -> Unit)?
        get() = { binding ->
            binding.activity = this
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
        }

    val adapterVideos = VideoAdapter {
        //    playYouTubeVideo(it)
    }
    val adapterCast = PersonAdapter(isCast = true)
    val adapterImages = ImageAdapter()
    val adapterRecommendations = TvAdapter()
    val adapterSeasons by lazy {
        //    SeasonAdapter(id)
    }


}