package com.nafanya.mp3world.features.allSongs.view

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searching.SearchableFragment
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allSongs.viewModel.AllSongsViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.playlist.baseViews.StatePlaylistHolderFragment
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog

class AllSongsFragment :
    StatePlaylistHolderFragment(),
    SearchableFragment<SongWrapper> {

    private val viewModel: AllSongsViewModel by viewModels { factory.get() }
    override val playlistViewModel: StatePlaylistViewModel
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
    override val playlistAdapter: BaseSongListAdapter
        get() = allSongsAdapter

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allSongsComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createTopBar(viewModel).invoke(menu, inflater)
    }
}
