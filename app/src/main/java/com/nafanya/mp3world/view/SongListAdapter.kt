package com.nafanya.mp3world.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.PlayerManager
import com.nafanya.mp3world.model.Song

class SongListAdapter(
    private val context: Context,
    private val list: ArrayList<Song>,
    private val preview: Boolean
) : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(itemView, context)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SongViewHolder(
        itemView: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = SongListItemBinding.bind(itemView)

        fun bind(song: Song) {
            binding.song = song
            binding.songListItem.setOnClickListener {
                PlayerManager.play(context, song)
            }
//            OnSwipeTouchListener(context, binding.songListItem) {
//                SongListFullScreenFragment
//            }
//            OnSwipeTouchListener(context, binding.title)
//            OnSwipeTouchListener(context, binding.artist)
        }
    }
}
