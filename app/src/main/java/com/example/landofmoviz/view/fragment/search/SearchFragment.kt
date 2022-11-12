package com.example.landofmoviz.view.fragment.search

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.landofmoviz.R
import com.example.landofmoviz.databinding.FragmentSearchBinding
import com.example.landofmoviz.utils.LifecycleRecyclerView
import com.example.landofmoviz.utils.MediaType
import com.example.landofmoviz.view.adapter.GenreAdapter
import com.example.landofmoviz.view.adapter.MovieAdapter
import com.example.landofmoviz.view.adapter.PersonAdapter
import com.example.landofmoviz.view.adapter.TvAdapter
import com.example.landofmoviz.view.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    val viewModel: SearchViewModel by viewModels()

    override val defineBindingVariables: ((FragmentSearchBinding) -> Unit)?
        get() = { binding ->
            binding.fragment = this
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }

    val adapterMovies by lazy { MovieAdapter() }
    val adapterTvs by lazy { TvAdapter() }
    val adapterPeople by lazy { PersonAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.apply {
            addObserver(LifecycleRecyclerView(binding.rvGenres))
            addObserver(LifecycleRecyclerView(binding.rvMovies))
            addObserver(LifecycleRecyclerView(binding.rvPeople))
            addObserver(LifecycleRecyclerView(binding.rvTvs))

        }

        setupSearchView()
        setupSpinner()
        collectFlows(
            listOf(
                ::collectMovieSearchResults,
                ::collectTvSearchResults,
                ::collectPersonSearchResults,
                ::collectUiState
            )
        )

    }

    private fun setupSearchView() {


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                binding.rvMovies.scrollToPosition(0)
                binding.rvTvs.scrollToPosition(0)
                binding.rvPeople.scrollToPosition(0)
                if (!p0.isNullOrEmpty()) viewModel.fetchInitialSearch(query = p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean = false

        })
    }

    private fun setupSpinner() {
        binding.spGenreMediaType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> {
                            val movieGenreIds =
                                resources.getIntArray(R.array.movie_genre_ids).toTypedArray()
                            val movieGenreNames =
                                resources.getStringArray(R.array.movie_genre_names)
                            binding.rvGenres.adapter = GenreAdapter(MediaType.MOVIE).apply {
                                submitList(movieGenreIds.zip(movieGenreNames))
                            }
                        }
                        1 -> {
                            val tvGenreIds =
                                resources.getIntArray(R.array.tv_genre_ids).toTypedArray()
                            val tvGenreNames = resources.getStringArray(R.array.tv_genre_names)
                            binding.rvGenres.adapter = GenreAdapter(MediaType.TV).apply {
                                submitList(
                                    tvGenreIds.zip(tvGenreNames)
                                )
                            }
                        }

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
    }


    private suspend fun collectMovieSearchResults() {
        viewModel.movieResults.collect { movies ->
            adapterMovies.submitList(movies)
        }
    }


    private suspend fun collectTvSearchResults() {

        viewModel.tvResults.collect { tvs ->
            adapterTvs.submitList(tvs)

        }
    }

    private suspend fun collectPersonSearchResults() {
        viewModel.personResults.collect { persons ->
            adapterPeople.submitList(persons)

        }
    }


    private suspend fun collectUiState(){
        viewModel.uiState.collect{ state->
          if(state.isError) showSnackbar(
              message = state.errorText!!,
              actionText = getString(R.string.button_retry),
              anchor = true
          ){
              viewModel.retryConnection {
                  viewModel.initRequests()
              }
          }
        }
    }



    fun clearSearch() {
        viewModel.clearSearchResults()
        adapterMovies.submitList(null)
        adapterTvs.submitList(null)
        adapterPeople.submitList(null)
    }
}