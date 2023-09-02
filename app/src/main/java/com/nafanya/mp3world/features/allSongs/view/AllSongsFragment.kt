package com.nafanya.mp3world.features.allSongs.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.listUtils.searching.attachToTopBar
import com.nafanya.mp3world.features.allSongs.viewModel.AllSongsViewModel
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongDialogHolder

class AllSongsFragment :
    StatedPlaylistFragmentBaseLayout(),
    SwipeRefreshLayout.OnRefreshListener,
    LocalSongDialogHolder {

    private val viewModel: AllSongsViewModel by viewModels { factory.get() }
    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override val actualFavouriteListViewModel: FavouriteListViewModel
        get() = favouriteListViewModel.get()

    private val allSongsAdapter: AllSongsAdapter by lazy {
        AllSongsAdapter(
            onSongItemClickCallback = ::onSongClick,
            onActionClickedCallback = { song ->
                val dialog = createDialog(requireActivity(), song)
                actualFavouriteListViewModel.isSongInFavourite(song).observe(viewLifecycleOwner) {
                    dialog.setIsFavorite(it)
                }
                dialog.show()
            }
        )
    }
    override val songListAdapter: BaseSongListAdapter
        get() = allSongsAdapter

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allSongsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
