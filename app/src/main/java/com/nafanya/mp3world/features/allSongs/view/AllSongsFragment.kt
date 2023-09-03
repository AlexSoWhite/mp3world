package com.nafanya.mp3world.features.allSongs.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searching.attachToTopBar
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.features.allSongs.viewModel.AllSongsViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.actionDialogs.defaultLocalSongActionDialog

class AllSongsFragment :
    StatedPlaylistFragmentBaseLayout(),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: AllSongsViewModel by viewModels { factory.get() }
    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    private val allSongsAdapter: AllSongsAdapter by lazy {
        AllSongsAdapter(
            onSongItemClickCallback = ::onSongClick,
            onActionClickedCallback = (requireActivity() as AppCompatActivity)
                .defaultLocalSongActionDialog(viewModel)
        )
    }
    override val songListAdapter: BaseSongListAdapter
        get() = allSongsAdapter

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allSongsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        }
        binding.refreshToggle.isEnabled = true
        binding.refreshToggle.setOnRefreshListener {
            binding.refreshToggle.isRefreshing = true
            onRefresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }

    override fun onRefresh() {
        viewModel.refresh()
        binding.refreshToggle.isRefreshing = false
    }
}
