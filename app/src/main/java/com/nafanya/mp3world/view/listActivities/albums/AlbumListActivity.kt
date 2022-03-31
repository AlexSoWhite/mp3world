package com.nafanya.mp3world.view.listActivities.albums

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.view.listActivities.RecyclerHolderActivity
import com.nafanya.mp3world.view.listActivities.songs.SongListActivity
import com.nafanya.mp3world.viewmodel.listViewModels.SourceProvider
import com.nafanya.mp3world.viewmodel.listViewModels.albums.AlbumListViewModel

class AlbumListActivity : RecyclerHolderActivity() {

    override fun setViewModel() {
        viewModel = ViewModelProvider(this)[AlbumListViewModel::class.java]
    }

    override fun setAdapter() {
        val observer = Observer<MutableList<Album>> {
            binding.recycler.adapter = AlbumListAdapter(it) { album ->
                val intent = Intent(this, SongListActivity::class.java)
                SourceProvider.newInstanceWithPlaylist(
                    Playlist(
                        album.songList,
                        0,
                        album.name
                    )
                )
                startActivity(intent)
            }
        }
        (viewModel as AlbumListViewModel).albumsList.observe(this, observer)
    }

    override fun onEmpty() {
        super.onEmpty()
        binding.emptyAlbumList.emptyAlbumList.visibility = View.VISIBLE
    }
}
