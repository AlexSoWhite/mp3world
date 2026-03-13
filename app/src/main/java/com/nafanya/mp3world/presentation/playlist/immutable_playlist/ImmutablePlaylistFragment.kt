package com.nafanya.mp3world.presentation.playlist.immutable_playlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.utils.list_utils.searching.attachToTopBar
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.song_list_views.action_dialogs.bottomSheetLocalSongActionDialog
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImmutablePlaylistFragment : StatedPlaylistFragmentBaseLayout() {

    @Inject
    lateinit var immutableFactory: ImmutablePlaylistViewModel.Factory.AssistedPlaylistFactory
    private val viewModel: ImmutablePlaylistViewModel by viewModels {
        immutableFactory.create(
            arguments?.getInt(ImmutablePlaylistActivity.LIST_MANAGER_KEY, -1) ?: -1,
            arguments?.getLong(ImmutablePlaylistActivity.CONTAINER_ID, -1) ?: -1,
            arguments?.getString(ImmutablePlaylistActivity.PLAYLIST_NAME),
            arguments?.getInt(ImmutablePlaylistActivity.PLAYLIST_NAME_RES, -1)?.takeIf { it > -1 }
        )
    }

    private val immutablePlaylistAdapter: ImmutablePlaylistAdapter by lazy {
        ImmutablePlaylistAdapter(
            onSongClickCallback = ::onSongClick,
            onActionClickedCallback = (requireActivity() as AppCompatActivity).bottomSheetLocalSongActionDialog()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.title.collectLatest {
                    val activity = requireActivity() as AppCompatActivity
                    activity.supportActionBar?.title = it.getText(activity)
                }
            }
        }
    }

    // todo(New API)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
