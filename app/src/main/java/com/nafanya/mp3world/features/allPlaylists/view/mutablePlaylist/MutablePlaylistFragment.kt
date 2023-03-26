package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.utils.attachToTopBar
import com.nafanya.mp3world.features.allPlaylists.viewModel.MutablePlaylistViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog
import javax.inject.Inject
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class MutablePlaylistFragment : StatedPlaylistFragmentBaseLayout() {

    @Inject
    lateinit var mutableFactory: MutablePlaylistViewModel.Factory.MutablePlaylistAssistedFactory

    private val viewModel: MutablePlaylistViewModel by viewModels {
        mutableFactory.create(
            arguments?.getLong(MutablePlaylistActivity.PLAYLIST_ID, -1) ?: -1,
            arguments?.getString(MutablePlaylistActivity.PLAYLIST_NAME) ?: ""
        )
    }
    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    private val mutablePlaylistAdapter: MutablePlaylistAdapter by lazy {
        MutablePlaylistAdapter(
            onSongClickCallback = { viewModel.onSongClick(it) },
            onModifyButtonClickCallback = {
                moveToModifyPlaylistActivity()
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

    override val songListAdapter: BaseSongListAdapter
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
            moveToModifyPlaylistActivity()
        }
    }

    private fun moveToModifyPlaylistActivity() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlist.take(1).collect {
                ActivityStarter.Builder()
                    .with(requireContext())
                    .createIntentToModifyPlaylistActivity(it)
                    .build()
                    .startActivity()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
