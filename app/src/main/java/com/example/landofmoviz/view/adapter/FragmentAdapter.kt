package com.example.landofmoviz.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.landofmoviz.utils.Constants
import com.example.landofmoviz.view.fragment.favourite.FavouriteFragment
import com.example.landofmoviz.view.fragment.favouritemovies.FavoriteMoviesFragment
import com.example.landofmoviz.view.fragment.favouritetvs.FavoriteTvsFragment
import com.example.landofmoviz.view.fragment.home.HomeFragment
import com.example.landofmoviz.view.fragment.movielists.MovieListsFragment
import com.example.landofmoviz.view.fragment.tvlists.TvListsFragment

class FragmentAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    private val homeFragments = listOf(MovieListsFragment(), TvListsFragment())
    private val favoritesFragments = listOf(FavoriteMoviesFragment(), FavoriteTvsFragment())


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (fragment) {
            is HomeFragment -> homeFragments[position]
            is FavouriteFragment -> favoritesFragments[position]
            else -> throw IllegalArgumentException(Constants.ILLEGAL_ARGUMENT_FRAGMENT_TYPE)
        }
    }


}