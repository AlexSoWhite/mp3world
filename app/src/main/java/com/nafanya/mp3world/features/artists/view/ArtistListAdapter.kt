package com.nafanya.mp3world.features.artists.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ArtistListItemBinding
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.core.utils.TextUtil

class ArtistListAdapter(
    private val list: MutableList<Artist>,
    private val callback: (artist: Artist) -> Unit
) : RecyclerView.Adapter<ArtistListAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(
            parent.context
        ).inflate(R.layout.artist_list_item, parent, false)
        return ArtistViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding = ArtistListItemBinding.bind(itemView)

        fun bind(artist: Artist, callback: (artist: Artist) -> Unit) {
            binding.artist = artist
            binding.tracksCount = TextUtil.getCompositionsCountString(
                artist.playlist?.songList?.size!!
            )
            if (artist.image != null) {
                binding.artistImage.setImageBitmap(artist.image)
            } else {
                binding.artistImage.setImageResource(R.drawable.artist)
            }
            binding.artistListItem.setOnClickListener {
                callback(artist)
            }
        }
    }
}
