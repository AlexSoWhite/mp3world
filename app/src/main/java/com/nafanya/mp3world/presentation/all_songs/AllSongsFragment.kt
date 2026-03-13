package com.nafanya.mp3world.presentation.all_songs

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.utils.list_utils.searching.attachToTopBar
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.song_list_views.action_dialogs.defaultLocalSongActionDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AllSongsFragment :
    StatedPlaylistFragmentBaseLayout(),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: AllSongsViewModel by viewModels { factory.get() }
    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    private val allSongsAdapter: AllSongsAdapter by lazy {
        AllSongsAdapter(
            onSongItemClickCallback = ::onSongClick,
            onActionClickedCallback = (requireActivity() as AppCompatActivity)
                .defaultLocalSongActionDialog(viewModel)
        )
    }
    override val songListAdapter: BaseSongListAdapter
        get() = allSongsAdapter

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allSongsComponent.inject(this)
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
        binding.refreshToggle.isEnabled = true
        binding.refreshToggle.setOnRefreshListener {
            binding.refreshToggle.isRefreshing = true
            onRefresh()
        }
    }

    // todo(New API)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }

    override fun onRefresh() {
        viewModel.refresh()
        binding.refreshToggle.isRefreshing = false
    }
}
