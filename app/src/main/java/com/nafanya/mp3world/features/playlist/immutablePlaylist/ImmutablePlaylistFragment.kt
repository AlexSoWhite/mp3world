package com.nafanya.mp3world.features.playlist.immutablePlaylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searching.attachToTopBar
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongDialogHolder
import javax.inject.Inject

class ImmutablePlaylistFragment :
    StatedPlaylistFragmentBaseLayout(),
    LocalSongDialogHolder {

    @Inject
    lateinit var immutableFactory: ImmutablePlaylistViewModel.Factory.AssistedPlaylistFactory
    private val viewModel: ImmutablePlaylistViewModel by viewModels {
        immutableFactory.create(
            arguments?.getInt(ImmutablePlaylistActivity.LIST_MANAGER_KEY, -1) ?: -1,
            arguments?.getLong(ImmutablePlaylistActivity.CONTAINER_ID, -1) ?: -1,
            arguments?.getString(ImmutablePlaylistActivity.PLAYLIST_NAME) ?: ""
        )
    }

    override val actualFavouriteListViewModel: FavouriteListViewModel
        get() = favouriteListViewModel.get()

    private val immutablePlaylistAdapter: ImmutablePlaylistAdapter by lazy {
        ImmutablePlaylistAdapter(
            onSongClickCallback = ::onSongClick,
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
        get() = immutablePlaylistAdapter

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.playlistComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
