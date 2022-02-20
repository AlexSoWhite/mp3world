package com.nafanya.mp3world.view.listActivities.artists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.ArtistListItemBinding
import com.nafanya.mp3world.model.wrappers.Artist

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
            binding.tracksCount = "(" + artist.playlist?.songList?.size.toString() + ")"
            binding.artistListItem.setOnClickListener {
                callback(artist)
            }
        }
    }
}
