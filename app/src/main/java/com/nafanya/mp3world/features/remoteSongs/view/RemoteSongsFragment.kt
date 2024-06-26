package com.nafanya.mp3world.features.remoteSongs.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.stateMachines.commonUi.list.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.stateMachines.commonUi.list.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.song.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.api.download
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.remoteSongs.view.RemoteSongsActivity.Companion.QUERY
import javax.inject.Inject

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
        viewModel.title.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
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
