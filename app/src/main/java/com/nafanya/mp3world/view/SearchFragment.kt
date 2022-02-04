package com.nafanya.mp3world.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SearchFragmentBinding
import com.nafanya.mp3world.model.Playlist
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.viewmodel.SearchViewModel

class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var binding: SearchFragmentBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.search_fragment,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.recyclerSearch.adapter = SongListAdapter(query.songList, activity) {
            ForegroundServiceLiveDataProvider.currentPlaylist.value =
                query
        }
        binding.recyclerSearch.layoutManager = LinearLayoutManager(activity)
        binding.recyclerSearch.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        return binding.root
    }

    companion object {

        private lateinit var query: Playlist

        fun newInstance(query: Playlist): SearchFragment {
            this.query = query
            return SearchFragment()
        }
    }
}
