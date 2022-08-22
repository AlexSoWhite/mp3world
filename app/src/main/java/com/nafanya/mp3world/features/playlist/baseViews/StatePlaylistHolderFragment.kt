package com.nafanya.mp3world.features.playlist.baseViews

import android.os.Bundle
import android.view.View
import androidx.core.view.allViews
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.StateFragment
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.mp3world.features.songListViews.baseViews.SongView

abstract class StatePlaylistHolderFragment : StateFragment<SongWrapper, SongListItem>() {

    abstract val playlistAdapter: BaseSongListAdapter
    final override val adapter: BaseAdapter<SongListItem, out BaseViewHolder>
        get() = playlistAdapter
    abstract val playlistViewModel: StatePlaylistViewModel
    final override val stateMachine: StateMachine<SongWrapper, SongListItem>
        get() = playlistViewModel
    override val emptyMockImageResource: Int
        get() = R.drawable.empty_song_list
    override val emptyMockTextResource: Int
        get() = R.string.no_songs

    private var currentPlayingView: SongView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistAdapter.onBound.observe(viewLifecycleOwner) {
            playlistViewModel.onFirstItemBound()
        }
        playlistViewModel.currentSong.observe(viewLifecycleOwner) { song ->
            binding.recycler.allViews.filter {
                it is SongView
            }.forEach {
                if ((it as SongView).updateCurrentSong(song as SongWrapper)) {
                    currentPlayingView = it
                }
            }
        }
        playlistViewModel.isPlaying.observe(viewLifecycleOwner) {
            currentPlayingView?.updateIsPlaying(it)
        }
    }
}
