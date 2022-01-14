package com.nafanya.mp3world.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.Song
import com.nafanya.mp3world.viewmodel.ForegroundServiceLiveDataProvider
import kotlin.collections.ArrayList

class SongListAdapter(
    private val list: ArrayList<Song>,
    private val context: LifecycleOwner? = null
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
                        binding.title.alpha = maxAlpha
                        binding.artist.alpha = maxAlpha
                        binding.dateHolder.alpha = maxAlpha
                    } else {
                        binding.title.alpha = minAlpha
                        binding.artist.alpha = minAlpha
                        binding.dateHolder.alpha = minAlpha
                    }
                }
                ForegroundServiceLiveDataProvider.currentSong.observe(
                    context,
                    observer
                )
            }
            binding.songListItem.setOnClickListener {
                ForegroundServiceLiveDataProvider.currentSong.value = song
            }
        }
    }

    companion object {
        const val minAlpha = 0.8F
        const val maxAlpha = 1.0F
    }
}
