package com.nafanya.mp3world.view

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.transition.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentSongListPreviewBinding
import com.nafanya.mp3world.model.Listener
import com.nafanya.mp3world.model.OnSwipeListener
import com.nafanya.mp3world.model.SongListManager
import com.nafanya.mp3world.viewmodel.SongListViewModel

class SongListPreviewFragment : Fragment(R.layout.fragment_song_list_preview) {

    private lateinit var binding: FragmentSongListPreviewBinding
    private lateinit var songListViewModel: SongListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_song_list_preview,
            container,
            false
        )
        songListViewModel = ViewModelProvider(this)[SongListViewModel::class.java]
        Listener.setSongListViewModel(songListViewModel)
        binding.songsRecycler.adapter = SongListAdapter(
            SongListManager.getSongList(),
            songListViewModel,
            this
        )
        binding.songsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.songsRecycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        OnSwipeListener(binding.songsRecycler) {
            shrink()
        }
        binding.songCount = SongListManager.getSongList().size
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        set(binding, binding.songsRecycler.layoutParams.height)
        binding.songsRecycler.addOnScrollListener(ScrollListener)
    }

    companion object ScrollListener : RecyclerView.OnScrollListener() {

        private lateinit var binding: FragmentSongListPreviewBinding
        private var rememberedHeight: Int = 0
        private const val toFullScreenAnimationDuration = 200L
        private const val fromFullScreenAnimationDuration = 100L
        private const val maxAlpha = 255
        var isFullScreen = false

        fun set(
            binding: FragmentSongListPreviewBinding,
            rememberedHeight: Int
        ) {
            this.binding = binding
            this.rememberedHeight = rememberedHeight
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isFullScreen && dy > 0) {
                val layoutParams = binding.songsRecycler.layoutParams
                val transition = AutoTransition()
                transition.duration = toFullScreenAnimationDuration
                transition.doOnEnd {
                    binding.songsRecycler.background.alpha = 0
                    isFullScreen = true
                }
                TransitionManager.beginDelayedTransition(binding.songsRecycler, transition)
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                binding.songsRecycler.layoutParams = layoutParams
            }
        }

        fun shrink() {
            if (isFullScreen) {
                val layoutParams = binding.songsRecycler.layoutParams
                val transition = AutoTransition()
                transition.duration = fromFullScreenAnimationDuration
                transition.doOnEnd {
                    isFullScreen = false
                    binding.songsRecycler.background.alpha = maxAlpha
                }
                TransitionManager.beginDelayedTransition(binding.songsRecycler, transition)
                layoutParams.height = rememberedHeight
                binding.songsRecycler.layoutParams = layoutParams
            }
        }
    }
}
