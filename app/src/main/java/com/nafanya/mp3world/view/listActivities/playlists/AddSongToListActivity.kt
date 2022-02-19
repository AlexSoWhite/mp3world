package com.nafanya.mp3world.view.listActivities.playlists

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.listManagers.MediaStoreReader
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.Song
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListAdapter
import com.nafanya.mp3world.viewmodel.PageState
import com.nafanya.mp3world.viewmodel.listViewModels.songs.SongListViewModel

class AddSongToListActivity : RecyclerHolderActivity() {
    override fun setAdapter() {
        binding.recycler.adapter = AddToPlaylistAdapter(SongListManager.songList) {

        }
    }

    override fun getActivityDescription(): String {
        return playlist!!.name
    }

    override fun addCustomBehavior() {
        super.addCustomBehavior()

    }

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        viewModel.pageState.value = PageState.IS_LOADED
    }

    class AddToPlaylistAdapter(
        private val list: ArrayList<Song>,
        private val context: LifecycleOwner? = null,
        private val callback: () -> Unit
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
