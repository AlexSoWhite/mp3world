package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentContainerBinding

class FragmentContainer : Fragment(R.layout.fragment_container) {

    private lateinit var binding: FragmentContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_container,
            container,
            false
        )
        binding.pager.subscribePaging(this)
        binding.pager.adapter = PagerAdapter(childFragmentManager)
        return binding.root
    }

    private inner class PagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = SongListFragment()
                1 -> fragment = PlaylistListFragment()
                2 -> fragment = ArtistListFragment()
            }
            return fragment!!
        }
    }
}
