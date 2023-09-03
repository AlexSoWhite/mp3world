package com.nafanya.mp3world.features.allPlaylists.modifyPlaylist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searching.DefaultOnQueryTextListener
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.features.allPlaylists.modifyPlaylist.ModifyPlaylistActivity.Companion.PLAYLIST_NAME
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import javax.inject.Inject

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
        viewModel.modifyingPlaylist.observe(viewLifecycleOwner) {
            modifyPlaylistAdapter.setModifyingPlaylist(it)
            modifyPlaylistAdapter.notifyDataSetChanged()
        }
        viewModel.title.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
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
