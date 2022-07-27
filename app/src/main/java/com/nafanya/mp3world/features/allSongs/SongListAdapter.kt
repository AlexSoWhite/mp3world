package com.nafanya.mp3world.features.allSongs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.utils.timeConverters.TimeConverter
import com.nafanya.mp3world.databinding.SongListItemBinding
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.player.Playlist
import com.nafanya.player.Song

/**
 * TODO: change to list adapter and separate logic
  */
open class SongListAdapter(
    private val playlist: Playlist,
    private val downloadViewModel: DownloadViewModel? = null,
    private val callback: (Playlist, Song) -> Unit
) : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {

    private val list = playlist.songList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    open fun decorateItem(
        binding: SongListItemBinding,
        song: Song
    ) {
        if (song.url != null) {
            // binding.action.visibility = View.VISIBLE
            // TODO move to viewModel
//            binding.action.setOnClickListener {
//                Toast.makeText(
//                    context as Context,
//                    "загрузка начата...",
//                    Toast.LENGTH_SHORT
//                ).show()
//                downloadViewModel?.download(song) {
//                    when (it.type) {
//                        ResultType.SUCCESS -> Toast.makeText(
//                            context as Context,
//                            "${song.artist} - ${song.title} загружено",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        ResultType.ERROR -> Toast.makeText(
//                            context as Context,
//                            "ошибка",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
           // Glide.with(binding.songListItem).load(R.drawable.download_icon).into(binding.action)
        } else {
            binding.action.visibility = View.GONE
        }
    }

    inner class SongViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = SongListItemBinding.bind(itemView)

        @Suppress("ComplexCondition")
        fun bind(
            song: Song
        ) {
            binding.song = song
//            val isPlayingObserver = Observer<Boolean> { isPlaying ->
//                val playingSong = PlayerLiveDataProvider.currentSong.value
//                if (isPlaying &&
//                    (
//                        playingSong?.id == song.id && playingSong.url == null ||
//                            playingSong?.url != null && playingSong.url == song.url
//                        )
//                ) {
//                    Glide.with(binding.playingIndicator)
//                        .load(R.drawable.pause)
//                        .into(binding.playingIndicator)
//                } else if (!isPlaying) {
//                    Glide.with(binding.playingIndicator)
//                        .load(R.drawable.play)
//                        .into(binding.playingIndicator)
//                } else {
//                    Glide.with(binding.playingIndicator)
//                        .load(R.drawable.play)
//                        .into(binding.playingIndicator)
//                }
//            }
//            PlayerLiveDataProvider.isPlaying.observe(context, isPlayingObserver)
            when {
                song.art != null -> {
                    binding.songIcon.setImageBitmap(song.art)
                }
                song.artUrl != null -> {
                    Glide.with(binding.songIcon)
                        .load(song.artUrl)
                        .into(binding.songIcon)
                }
                else -> {
                    binding.songIcon.setImageResource(R.drawable.music_menu_icon)
                }
            }
            binding.duration.text = song.duration?.let { TimeConverter().durationToString(it) }
            binding.songListItem.setOnClickListener {
                callback(playlist, song)
            }
            decorateItem(
                binding,
                song
            )
        }
    }
}
