package com.nafanya.mp3world.view

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.databinding.FragmentPlaylistListPreviewBinding
import com.nafanya.mp3world.model.OnSwipeTouchListener
import com.nafanya.mp3world.model.SongListManager

class PlaylistListPreviewFragment : Fragment(R.layout.fragment_playlist_list_preview) {

    private lateinit var binding: FragmentPlaylistListPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_playlist_list_preview,
            container,
            false
        )
        binding.playlistsRecycler.adapter = SongListAdapter(
            SongListManager.getSongList()
        )
        binding.playlistsRecycler.layoutManager = LinearLayoutManager(activity)
        binding.playlistsRecycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        OnSwipeTouchListener(
            requireActivity(),
            binding.playlistsRecycler
        ) {
            shrink()
        }
        binding.playlistCount = SongListManager.getSongList().size
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        set(binding, binding.playlistsRecycler.layoutParams.height, requireActivity())
        binding.playlistsRecycler.addOnScrollListener(ScrollListener)
    }

    companion object ScrollListener : RecyclerView.OnScrollListener() {

        private lateinit var binding: FragmentPlaylistListPreviewBinding
        private lateinit var activity: FragmentActivity
        private const val toFullScreenAnimationDuration = 300L
        private const val fromFullScreenAnimationDuration = 300L
        private const val maxAlpha = 255
        private var rememberedHeight: Int = 0
        var isFullScreen = false

        fun set(
            binding: FragmentPlaylistListPreviewBinding,
            rememberedHeight: Int,
            activity: FragmentActivity
        ) {
            this.binding = binding
            this.rememberedHeight = rememberedHeight
            this.activity = activity
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isFullScreen && dy > 0) {
                val layoutParams = binding.playlistsRecycler.layoutParams
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                val constraintSet = ConstraintSet()
                val mainView = activity.findViewById<ConstraintLayout>(R.id.recyclers_container)
                constraintSet.clone(mainView)
                val transition = AutoTransition()
                transition.duration = toFullScreenAnimationDuration
                transition.doOnEnd {
                    binding.playlistsRecycler.background.alpha = 0
                    isFullScreen = true
                }
                TransitionManager.beginDelayedTransition(mainView, transition)
                constraintSet.connect(
                    R.id.playlists_list_container,
                    ConstraintSet.TOP,
                    R.id.recyclers_container,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(mainView)
                binding.playlistsRecycler.layoutParams = layoutParams
            }
        }

        fun shrink() {
            if (isFullScreen) {
                val layoutParams = binding.playlistsRecycler.layoutParams
                layoutParams.height = rememberedHeight
                binding.playlistsRecycler.layoutParams = layoutParams
                val constraintSet = ConstraintSet()
                val mainView = activity.findViewById<ConstraintLayout>(R.id.recyclers_container)
                constraintSet.clone(mainView)
                val transition = AutoTransition()
                transition.duration = fromFullScreenAnimationDuration
                transition.doOnEnd {
                    binding.playlistsRecycler.background.alpha = maxAlpha
                    isFullScreen = false
                }
                TransitionManager.beginDelayedTransition(mainView, transition)
                constraintSet.clone(mainView)
                constraintSet.connect(
                    R.id.playlists_list_container,
                    ConstraintSet.TOP,
                    R.id.songs_list_container,
                    ConstraintSet.BOTTOM
                )
                constraintSet.applyTo(mainView)
            }
        }
    }
}
