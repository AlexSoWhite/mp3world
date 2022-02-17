package com.nafanya.mp3world.view.legacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.LegacyFragmentContainerBinding

@Deprecated(message = "Since project switched to multiple activities model, this class is no longer needed")
class FragmentContainer : Fragment(R.layout.legacy_fragment_container) {

    private lateinit var binding: LegacyFragmentContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.legacy_fragment_container,
            container,
            false
        )
        binding.pager.subscribePaging(this)
        binding.pager.adapter = PagerAdapter(childFragmentManager)
        return binding.root
    }

    private inner class PagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {

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
