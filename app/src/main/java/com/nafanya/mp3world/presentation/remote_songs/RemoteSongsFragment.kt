package com.nafanya.mp3world.presentation.remote_songs

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.data.downloading.api.download
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.remote_songs.RemoteSongsActivity.Companion.QUERY
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RemoteSongsFragment :
    StatedPlaylistFragmentBaseLayout(),
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var remoteSongsFactory: RemoteSongsViewModel.Factory.RemoteAssistedFactory

    private val viewModel: RemoteSongsViewModel by viewModels {
        remoteSongsFactory.create(
            arguments?.getString(QUERY)!!
        )
    }

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    private val remoteSongsAdapter = RemoteSongsAdapter(
        onSongClickCallback = ::onSongClick,
        onActionClickCallback = {
            download(viewModel, it as RemoteSong)
        }
    )

    override val songListAdapter: BaseSongListAdapter
        get() = remoteSongsAdapter

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.remoteSongsComponent.inject(this)
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

    override fun onRefresh() {
        viewModel.refresh()
        binding.refreshToggle.isRefreshing = false
    }
}
