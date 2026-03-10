package com.nafanya.mp3world.presentation.user_playlists.modify_playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.utils.list_utils.searching.DefaultOnQueryTextListener
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.presentation.user_playlists.modify_playlist.ModifyPlaylistActivity.Companion.PLAYLIST_NAME
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ModifyPlaylistFragment : StatedPlaylistFragmentBaseLayout() {

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
            onSongClickCallback = ::onSongClick,
            onSongAddCallback = { viewModel.addSongToCopyOfPlaylist(it) },
            onSongRemoveCallback = { viewModel.removeSongFromCopyOfPlaylist(it) }
        )
    }
    override val songListAdapter: BaseSongListAdapter
        get() = modifyPlaylistAdapter

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allPlaylistsComponent.inject(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.playlistUnderModification.collectLatest {
                        modifyPlaylistAdapter.setModifyingPlaylist(it)
                        modifyPlaylistAdapter.notifyDataSetChanged()
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.title.collectLatest {
                        val activity = requireActivity() as AppCompatActivity
                        activity.supportActionBar?.title = it.getText(activity)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = DiscardChangesDialog(
                    requireActivity()
                ) {
                    requireActivity().finish()
                }
                dialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        // setting appBar search view
        inflater.inflate(R.menu.add_songs_to_playlist_menu, menu)
        val confirm = menu.findItem(R.id.confirm_adding)
        val searchItem = menu.findItem(R.id.search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        // setting search dispatcher
        searchView.setOnQueryTextListener(DefaultOnQueryTextListener(viewModel))
        confirm?.setOnMenuItemClickListener {
            viewModel.confirmChanges()
            requireActivity().finish()
            true
        }
    }
}
