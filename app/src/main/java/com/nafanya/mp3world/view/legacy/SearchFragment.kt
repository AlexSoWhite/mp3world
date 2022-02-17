package com.nafanya.mp3world.view.legacy

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
import com.nafanya.mp3world.databinding.LegacySearchFragmentBinding
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.viewmodel.legacy.SearchViewModel

@Deprecated(message = "Use SongListActivity with search results instead")
class SearchFragment : Fragment(R.layout.legacy_search_fragment) {

    private lateinit var binding: LegacySearchFragmentBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.legacy_search_fragment,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        binding.recyclerSearch.adapter = SongListAdapter(playlist.songList, activity) {
            ForegroundServiceLiveDataProvider.currentPlaylist.value =
                playlist
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

        private lateinit var playlist: Playlist

        fun newInstance(query: Playlist): SearchFragment {
            Companion.playlist = query
            return SearchFragment()
        }
    }
}
