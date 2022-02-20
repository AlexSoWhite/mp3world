package com.nafanya.mp3world.view.listActivities.playlists

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class AddSongToListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<Playlist> {
            binding.recycler.adapter = AddToPlaylistAdapter(it.songList) {
                ForegroundServiceLiveDataProvider.currentPlaylist.value = it
            }
        }
        (viewModel as SongListViewModel).playlist.observe(this, observer)
    }

//    override fun setTitle() {
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
//        supportActionBar?.title = playlist!!.name
//    }

    override fun addCustomBehavior() {
        super.addCustomBehavior()
    }

    class AddToPlaylistAdapter(
        list: MutableList<Song>,
        context: LifecycleOwner? = null,
        callback: () -> Unit
    ) : SongListAdapter(list, context, callback) {

        override fun decorateItem(binding: SongListItemBinding, song: Song) {
            playlist?.songList?.forEach {
                if (it.id == song.id) return
            }
            Glide.with(binding.action).load(R.drawable.add).into(binding.action)
        }
    }

    companion object {

        private var playlist: Playlist? = null

        fun newInstance(playlist: Playlist) {
            this.playlist = playlist
        }
    }
}
