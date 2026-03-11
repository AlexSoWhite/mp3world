package com.nafanya.mp3world.core.state_machines.presentation.list.playlist

import android.os.Bundle
import android.view.View
import androidx.core.view.allViews
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.presentation.playlist.base_views.BaseSongListAdapter
import com.nafanya.mp3world.presentation.song_list_views.SongListItem
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongView
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

abstract class StatedPlaylistFragmentBaseLayout :
    StatedListFragmentBaseLayout<
        SongWrapper,
        SongListItem
        >() {

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    abstract val playlistViewModel: StatedPlaylistViewModel
    final override val listViewModel: StatedListViewModel<SongWrapper, SongListItem>
        get() = playlistViewModel

    abstract val songListAdapter: BaseSongListAdapter
    final override val adapter: BaseAdapter<SongListItem, out BaseViewHolder>
        get() = songListAdapter

    override val emptyMockImageResource: Int
        get() = R.drawable.icv_song_list
    override val emptyMockTextResource: Int
        get() = R.string.no_songs

    private var currentPlayingView: SongView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.init()
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    // todo: do something else when null?
                    playlistViewModel.currentSong.filterNotNull().collectLatest { song ->
                        // remove indicator from old view
                        currentPlayingView?.updateCurrentSong(song)
                        binding.recycler.allViews.filter {
                            it is SongView
                        }.forEach { view ->
                            (view as SongView).updateCurrentSong(song)
                        }
                        songListAdapter.setCurrentPlayingSong(song)
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    playlistViewModel.isPlaying.collectLatest { currentPlayingView?.updateIsPlaying(it) }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    songListAdapter.currentSelectedView.collectLatest {
                        currentPlayingView = it
                        currentPlayingView?.updateIsPlaying(playlistViewModel.isPlaying.value)
                    }
                }
            }
        }
    }

    fun onSongClick(song: SongWrapper, view: SongView) {
        playlistViewModel.onSongClick(song)
        currentPlayingView?.updateCurrentSong(song)
        currentPlayingView = view
    }
}
