package com.nafanya.mp3world.features.remoteSongs.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.playlist.StatedPlaylistViewModel
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.playlist.baseViews.StatePlaylistHolderFragment
import com.nafanya.mp3world.features.remoteSongs.view.RemoteSongsActivity.Companion.QUERY
import com.nafanya.mp3world.features.songListViews.actionDialogs.RemoteSongActionDialog
import javax.inject.Inject

class RemoteSongsFragment : StatePlaylistHolderFragment(), SwipeRefreshLayout.OnRefreshListener {

    private val remoteSongsAdapter = RemoteSongsAdapter(
        onSongClickCallback = { viewModel.onSongClick(it) },
        onActionClickCallback = {
            val dialog = RemoteSongActionDialog(
                requireContext(),
                it as RemoteSong,
                downloadViewModel.get()
            )
            dialog.show()
        }
    )
    override val playlistAdapter: BaseSongListAdapter
        get() = remoteSongsAdapter

    @Inject
    lateinit var remoteSongsFactory: RemoteSongsViewModel.Factory.RemoteAssistedFactory

    private val viewModel: RemoteSongsViewModel by viewModels {
        remoteSongsFactory.create(
            arguments?.getString(QUERY)!!
        )
    }

    override val playlistViewModel: StatedPlaylistViewModel
        get() = viewModel

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.remoteSongsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(requireActivity() as AppCompatActivity) {
            supportActionBar?.title = arguments?.getString(QUERY)
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
