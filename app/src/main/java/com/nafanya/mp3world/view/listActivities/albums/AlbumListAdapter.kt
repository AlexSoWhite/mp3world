package com.nafanya.mp3world.view.listActivities.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.AlbumListItemBinding
import com.nafanya.mp3world.model.wrappers.Album

class AlbumListAdapter(
    private val list: MutableList<Album>,
    private val callback: (Album) -> Unit
) : RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.album_list_item, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding = AlbumListItemBinding.bind(itemView)

        fun bind(album: Album, callback: (Album) -> Unit) {
            binding.album = album
            binding.tracksCount = album.songList.size
            binding.albumListItem.setOnClickListener {
                callback(album)
            }
        }
    }
}
