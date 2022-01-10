package com.nafanya.mp3world.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.model.SongListManager

class SongListAdapter(
    private val context: Context,
    private val list: ArrayList<Song>
) : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    private var lastDate: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(list[position])
        if (list[position].date != lastDate) {
            lastDate = list[position].date
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SongViewHolder(
        itemView: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemView) {

        companion object {
            private var usedSongBinding: SongListItemBinding? = null
        }

        private val binding = SongListItemBinding.bind(itemView)

        fun bind(song: Song) {
            binding.song = song
            binding.songListItem.setOnClickListener {
                SongListManager.startPlaying(context, song)
                binding.root.alpha = 0.80F
                usedSongBinding?.root?.alpha = 1F
                usedSongBinding = binding
            }
        }
    }
}
