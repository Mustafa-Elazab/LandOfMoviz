package com.example.landofmoviz.view.fragment.favouritetvs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.landofmoviz.R
import com.example.landofmoviz.databinding.FragmentFavoriteTvsBinding
import com.example.landofmoviz.domain.model.FavoriteTv
import com.example.landofmoviz.utils.LifecycleRecyclerView
import com.example.landofmoviz.view.adapter.FavoriteTvAdapter
import com.example.landofmoviz.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteTvsFragment :
    BaseFragment<FragmentFavoriteTvsBinding>(R.layout.fragment_favorite_tvs) {

    val viewModel: FavoriteTvsViewModel by activityViewModels()

    override val defineBindingVariables: ((FragmentFavoriteTvsBinding) -> Unit)?
        get() = { binding ->
            binding.fragment = this
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

    val adapterFavorites = FavoriteTvAdapter { removeTv(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleRecyclerView(binding.recyclerView))
        collectFlows(listOf(::collectFavoriteTvs))
    }


    private suspend fun collectFavoriteTvs() {
        viewModel.favoriteTvs.collect { favoriteTvs ->
            adapterFavorites.submitList(favoriteTvs)
        }
    }

    private fun removeTv(tv: FavoriteTv) {
        viewModel.removeTvFromFavorites(tv = tv)
        showSnackbar(
            message = getString(R.string.snackbar_removed_item),
            actionText = getString(R.string.snackbar_action_undo),
            anchor = true
        ) {
            viewModel.addTvToFavorites(tv)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFavoriteTvs()
    }
}