package com.nafanya.mp3world.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.PlayerManager
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.viewmodel.SongListViewModel
import kotlin.collections.ArrayList

class SongListAdapter(
    private val list: ArrayList<Song>,
    private val songListViewModel: SongListViewModel? = null,
    private val context: LifecycleOwner? = null
) : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    private var lastDate: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(itemView, songListViewModel, context)
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
        private val songListViewModel: SongListViewModel?,
        private val context: LifecycleOwner?
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = SongListItemBinding.bind(itemView)

        fun bind(
            song: Song
        ) {
            binding.song = song
            context?.let { context ->
                val observer = Observer<Song> {
                    if (it.id == song.id) {
                        binding.songListItem.setBackgroundResource(
                            R.drawable.rounded_rectangle_background
                        )
                    } else {
                        binding.songListItem.setBackgroundResource(
                            R.drawable.empty_background
                        )
                    }
                }
                songListViewModel!!.currentSong.observe(
                    context,
                    observer
                )
            }
            binding.songListItem.setOnClickListener {
                PlayerManager.moveToSong(song)
                PlayerManager.play()
            }
        }
    }
}
