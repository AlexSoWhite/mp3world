package com.nafanya.mp3world.features.remoteSongs.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.playlist.StatedPlaylistFragmentBaseLayout
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.features.downloading.DownloadingView
import com.nafanya.mp3world.features.downloading.DownloadingViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.remoteSongs.view.RemoteSongsActivity.Companion.QUERY
import javax.inject.Inject

class RemoteSongsFragment :
    StatedPlaylistFragmentBaseLayout(),
    SwipeRefreshLayout.OnRefreshListener,
    DownloadingView {

    @Inject
    lateinit var remoteSongsFactory: RemoteSongsViewModel.Factory.RemoteAssistedFactory

    override val songListAdapter: BaseSongListAdapter
        get() = remoteSongsAdapter

    private val remoteSongsAdapter = RemoteSongsAdapter(
        onSongClickCallback = ::onSongClick,
        onActionClickCallback = {
            download(requireActivity(), it as RemoteSong)
        }
    )

    private val viewModel: RemoteSongsViewModel by viewModels {
        remoteSongsFactory.create(
            arguments?.getString(QUERY)!!
        )
    }

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override val downloadingViewModel: DownloadingViewModel
        get() = viewModel

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
