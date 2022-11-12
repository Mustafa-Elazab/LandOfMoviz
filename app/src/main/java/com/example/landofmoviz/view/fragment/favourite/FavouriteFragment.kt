package com.example.landofmoviz.view.fragment.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.landofmoviz.R
import com.example.landofmoviz.databinding.FragmentFavouriteBinding
import com.example.landofmoviz.utils.LifecycleViewPager
import com.example.landofmoviz.view.adapter.FragmentAdapter
import com.example.landofmoviz.view.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(R.layout.fragment_favourite) {

    override val defineBindingVariables: ((FragmentFavouriteBinding) -> Unit)?
        get() = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = FragmentAdapter(this)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleViewPager(binding.viewPager))

        mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabTitles = listOf(getString(R.string.tab_title_1), getString(R.string.tab_title_2))
            tab.text = tabTitles[position]
        }

        mediator?.attach()
    }
}