package com.nafanya.mp3world.features.allSongs.view

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.attachToTopBar
import com.nafanya.mp3world.features.allSongs.viewModel.AllSongsViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog

class AllSongsFragment : StatedPlaylistFragmentBaseLayout() {

    private val viewModel: AllSongsViewModel by viewModels { factory.get() }
    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    private val allSongsAdapter: AllSongsAdapter by lazy {
        AllSongsAdapter(
            onSongItemClickCallback = { viewModel.onSongClick(it) },
            onActionClickedCallback = {
                val dialog = LocalSongActionDialog(
                    requireActivity(),
                    it,
                    favouriteListViewModel.get()
                )
                dialog.show()
            }
        )
    }
    override val songListAdapter: BaseSongListAdapter
        get() = allSongsAdapter

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allSongsComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
