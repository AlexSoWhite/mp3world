package com.nafanya.mp3world.features.playlist.immutablePlaylist

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.attachToTopBar
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog
import javax.inject.Inject

class ImmutablePlaylistFragment : StatedPlaylistFragmentBaseLayout() {

    @Inject
    lateinit var immutableFactory: ImmutablePlaylistViewModel.Factory.AssistedPlaylistFactory
    private val viewModel: ImmutablePlaylistViewModel by viewModels {
        immutableFactory.create(
            arguments?.getInt(ImmutablePlaylistActivity.LIST_MANAGER_KEY, -1) ?: -1,
            arguments?.getLong(ImmutablePlaylistActivity.CONTAINER_ID, -1) ?: -1,
            arguments?.getString(ImmutablePlaylistActivity.PLAYLIST_NAME) ?: ""
        )
    }

    private val immutablePlaylistAdapter: ImmutablePlaylistAdapter by lazy {
        ImmutablePlaylistAdapter(
            onSongClickCallback = { viewModel.onSongClick(it) },
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
        get() = immutablePlaylistAdapter

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.playlistComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
