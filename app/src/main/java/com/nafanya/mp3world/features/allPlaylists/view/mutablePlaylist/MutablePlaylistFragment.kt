package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searching.SearchableFragment
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.viewModel.MutablePlaylistViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.playlist.baseViews.StatePlaylistHolderFragment
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog
import javax.inject.Inject

class MutablePlaylistFragment :
    StatePlaylistHolderFragment(),
    SearchableFragment<SongWrapper> {

    @Inject
    lateinit var mutableFactory: MutablePlaylistViewModel.Factory.MutablePlaylistAssistedFactory

    private val viewModel: MutablePlaylistViewModel by viewModels {
        mutableFactory.create(
            arguments?.getLong(MutablePlaylistActivity.PLAYLIST_ID, -1) ?: -1
        )
    }
    override val playlistViewModel: StatePlaylistViewModel
        get() = viewModel

    private val mutablePlaylistAdapter: MutablePlaylistAdapter by lazy {
        MutablePlaylistAdapter(
            onSongClickCallback = { viewModel.onSongClick(it) },
            onModifyButtonClickCallback = {
                viewModel.playlist.value?.let {
                    ActivityStarter.Builder()
                        .with(requireContext())
                        .createIntentToModifyPlaylistActivity(it)
                        .build()
                        .startActivity()
                }
            },
            onLongPressCallback = { },
            onConfirmChangesCallback = { },
            onActionClickCallback = {
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
        get() = mutablePlaylistAdapter

    override val emptyMockImageResource: Int
        get() = R.drawable.playlist_add
    override val emptyMockTextResource: Int
        get() = R.string.add_songs

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allPlaylistsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emptyMock.root.setOnClickListener {
            viewModel.playlist.value?.let {
                ActivityStarter
                    .Builder()
                    .with(requireContext())
                    .createIntentToModifyPlaylistActivity(it)
                    .build()
                    .startActivity()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createTopBar(viewModel).invoke(menu, inflater)
    }
}
