package com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.attachToConfirmableTopBar
import com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist.ModifyPlaylistActivity.Companion.PLAYLIST_NAME
import com.nafanya.mp3world.features.allPlaylists.viewModel.ModifyPlaylistViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import javax.inject.Inject

class ModifyPlaylistFragment : StatedPlaylistFragmentBaseLayout() {

    private var isFinished = false

    @Inject
    lateinit var modifyFactory: ModifyPlaylistViewModel.Factory.ModifyPlaylistFactory
    private val viewModel: ModifyPlaylistViewModel by viewModels {
        modifyFactory.create(
            arguments?.getLong(ModifyPlaylistActivity.PLAYLIST_ID, -1) ?: -1,
            arguments?.getString(PLAYLIST_NAME) ?: ""
        )
    }

    private val modifyPlaylistAdapter: ModifyPlaylistAdapter by lazy {
        ModifyPlaylistAdapter(
            onSongClickCallback = { viewModel.onSongClick(it) },
            onSongAddCallback = { viewModel.addSongToPlaylist(it) },
            onSongRemoveCallback = { viewModel.removeSongFromPlaylist(it) }
        )
    }
    override val songListAdapter: BaseSongListAdapter
        get() = modifyPlaylistAdapter

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allPlaylistsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.modifyingPlaylist.observe(viewLifecycleOwner) {
            if (!isFinished) {
                modifyPlaylistAdapter.setModifyingPlaylist(it)
                modifyPlaylistAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToConfirmableTopBar(
            requireActivity() as AppCompatActivity,
        ) {
            viewModel.confirmChanges()
            requireActivity().finish()
            isFinished = true
        }.invoke(menu, inflater)
    }
}
