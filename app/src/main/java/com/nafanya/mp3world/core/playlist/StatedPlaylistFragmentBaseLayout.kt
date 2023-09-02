package com.nafanya.mp3world.core.playlist

import android.os.Bundle
import android.view.View
import androidx.core.view.allViews
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.stateMachines.list.StatedListFragmentBaseLayout
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.SongListItem
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject

abstract class StatedPlaylistFragmentBaseLayout :
    StatedListFragmentBaseLayout<
        SongWrapper,
        SongListItem
        >(),
    PlaylistHolder {

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    final override val listViewModel: StatedListViewModel<SongWrapper, SongListItem>
        get() = playlistViewModel

    final override val adapter: BaseAdapter<SongListItem, out BaseViewHolder>
        get() = songListAdapter

    override val emptyMockImageResource: Int
        get() = R.drawable.icv_song_list
    override val emptyMockTextResource: Int
        get() = R.string.no_songs

    private var currentPlayingView: SongView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.bindInteractor(playerInteractor)
        playlistViewModel.currentSong.observe(viewLifecycleOwner) { song ->
            // remove indicator from old view
            currentPlayingView?.updateCurrentSong(song)
            binding.recycler.allViews.filter {
                it is SongView
            }.forEach { view ->
                (view as SongView).updateCurrentSong(song)
            }
            songListAdapter.setCurrentPlayingSong(song)
        }
        songListAdapter.currentSelectedView.observe(viewLifecycleOwner) {
            currentPlayingView = it
            currentPlayingView?.updateIsPlaying(playlistViewModel.isPlaying.value!!)
        }
        playlistViewModel.isPlaying.observe(viewLifecycleOwner) {
            currentPlayingView?.updateIsPlaying(it)
        }
    }

    fun onSongClick(song: SongWrapper, view: SongView) {
        playlistViewModel.onSongClick(song)
        currentPlayingView?.updateCurrentSong(song)
        currentPlayingView = view
    }
}
