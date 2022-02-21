package com.nafanya.mp3world.view.listActivities.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.model.foregroundService.ForegroundServiceLiveDataProvider
import com.nafanya.mp3world.model.wrappers.Song

open class SongListAdapter(
    private val list: MutableList<Song>,
    private val context: LifecycleOwner,
    private val callback: () -> Unit
) : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(itemView, context) { binding, song ->
            decorateItem(binding, song)
        }
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    open fun decorateItem(
        binding: SongListItemBinding,
        song: Song
    ) {
        if (song.url != null) {
            binding.action.visibility = View.VISIBLE
            binding.action.setOnClickListener {}
            Glide.with(binding.songListItem).load(R.drawable.download_icon).into(binding.action)
        } else {
            binding.action.visibility = View.GONE
        }
    }

    class SongViewHolder(
        itemView: View,
        private val context: LifecycleOwner,
        private val decorateItem: (
            binding: SongListItemBinding,
            song: Song
        ) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = SongListItemBinding.bind(itemView)

        fun bind(
            song: Song,
            callback: () -> Unit
        ) {
            binding.song = song
            val isPlayingObserver = Observer<Boolean> { isPlaying ->
                if (isPlaying &&
                    ForegroundServiceLiveDataProvider.currentSong.value?.id == song.id
                ) {
                    Glide.with(binding.playingIndicator)
                        .load(R.drawable.pause)
                        .into(binding.playingIndicator)
                } else if (!isPlaying) {
                    Glide.with(binding.playingIndicator)
                        .load(R.drawable.play)
                        .into(binding.playingIndicator)
                } else {
                    Glide.with(binding.playingIndicator)
                        .load(R.drawable.play)
                        .into(binding.playingIndicator)
                }
            }
            ForegroundServiceLiveDataProvider.isPlaying.observe(context, isPlayingObserver)
            binding.songListItem.setOnClickListener {
                callback()
                ForegroundServiceLiveDataProvider.currentSong.value = song
            }
            decorateItem(
                binding,
                song
            )
        }
    }
}
