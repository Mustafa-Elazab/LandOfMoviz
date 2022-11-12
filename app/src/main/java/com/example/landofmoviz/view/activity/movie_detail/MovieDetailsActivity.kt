package com.example.landofmoviz.view.activity.movie_detail

import com.example.landofmoviz.view.adapter.ImageAdapter
import android.os.Bundle
import androidx.activity.viewModels
import com.example.landofmoviz.R
import com.example.landofmoviz.databinding.ActivityMovieDetailsBinding
import com.example.landofmoviz.view.activity.BaseActivity
import com.example.landofmoviz.view.adapter.MovieAdapter
import com.example.landofmoviz.view.adapter.PersonAdapter
import com.example.landofmoviz.view.adapter.VideoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : BaseActivity<ActivityMovieDetailsBinding>(R.layout.activity_movie_details) {

    private val viewModel: MovieDetailsViewModel by viewModels()

    override val defineBindingVariables: (ActivityMovieDetailsBinding) -> Unit
        get() = { binding ->
            binding.activity = this
            binding.lifecycleOwner = this
            binding.viewModel = viewModel
        }

    val adapterVideos = VideoAdapter {
       // playYouTubeVideo(it)
    }
    val adapterCast = PersonAdapter(isCast = true)
    val adapterImages = ImageAdapter()
    val adapterRecommendations = MovieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        viewModel.initRequests(id)
        collectFlows(listOf(::collectDetails, ::collectUiState))
    }

    private suspend fun collectDetails() {
        viewModel.details.collect { details ->
            adapterCast.submitList(details.credits.cast)
            adapterVideos.submitList(details.videos.filterVideos())
            adapterImages.submitList(details.images.backdrops)
            adapterRecommendations.submitList(details.recommendations.results)
        }
    }

    private suspend fun collectUiState() {
        viewModel.uiState.collect { state ->
            if (state.isError) showSnackbar(state.errorText!!, getString(R.string.button_retry)) {
                viewModel.retryConnection {
                    viewModel.initRequests(id)
                }
            }
        }
    }
}