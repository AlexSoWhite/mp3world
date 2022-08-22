package com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searchableConfirmable.SearchableConfirmableFragment
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.viewModel.ModifyPlaylistViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.playlist.baseViews.StatePlaylistHolderFragment
import javax.inject.Inject

class ModifyPlaylistFragment :
    StatePlaylistHolderFragment(),
    SearchableConfirmableFragment<SongWrapper> {

    @Inject
    lateinit var modifyFactory: ModifyPlaylistViewModel.Factory.ModifyPlaylistFactory
    private val viewModel: ModifyPlaylistViewModel by viewModels {
        modifyFactory.create(
            arguments?.getLong(ModifyPlaylistActivity.PLAYLIST_ID, -1) ?: -1
        )
    }

    private val modifyPlaylistAdapter: ModifyPlaylistAdapter by lazy {
        ModifyPlaylistAdapter(
            modifyingPlaylist = viewModel.modifyingPlaylist.value!!,
            onSongClickCallback = { viewModel.onSongClick(it) },
            onSongAddCallback = { viewModel.addSongToPlaylist(it) },
            onSongRemoveCallback = { viewModel.removeSongFromPlaylist(it) }
        )
    }
    override val playlistAdapter: BaseSongListAdapter
        get() = modifyPlaylistAdapter

    override val playlistViewModel: StatePlaylistViewModel
        get() = viewModel

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allPlaylistsComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createTopBar(
            this,
            viewModel,
            onConfirmCallback = {
                viewModel.confirmChanges()
                requireActivity().finish()
            }
        ).invoke(menu, inflater)
    }
}
