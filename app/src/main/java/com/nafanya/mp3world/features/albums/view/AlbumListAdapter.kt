package com.nafanya.mp3world.features.albums.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.TextUtil
import com.nafanya.mp3world.databinding.AlbumListItemBinding
import com.nafanya.mp3world.features.albums.Album

class AlbumListAdapter(
    private val list: List<Album>,
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
            binding.tracksCount = TextUtil.getCompositionsCountString(
                album.playlist!!.songList.size
            )
            if (album.image != null) {
                binding.albumImage.setImageBitmap(album.image)
            } else {
                binding.albumImage.setImageResource(R.drawable.album)
            }
            binding.albumListItem.setOnClickListener {
                callback(album)
            }
        }
    }
}
