package com.nafanya.mp3world.presentation.player_view

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.RepeatModeUtil
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.presentation.core.common_ui.BaseFragment
import com.nafanya.mp3world.core.wrappers.song.SongWrapper
import com.nafanya.mp3world.core.wrappers.song.joinArtists
import com.nafanya.mp3world.databinding.PlayerControlViewBottomFragmentBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BottomControlViewFragment : BaseFragment<PlayerControlViewBottomFragmentBinding>() {

    private companion object {
        const val TAG = "_BottomControlView"
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: PlayerViewModel by viewModels {
        factory
    }

    override fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): PlayerControlViewBottomFragmentBinding {
        return PlayerControlViewBottomFragmentBinding.inflate(inflater, parent, attachToParent)
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as PlayerApplication)
            .applicationComponent
            .playerViewComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playerControlView.showTimeoutMs = 0
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.isPlayerReady.collectLatest {
                        Log.d(TAG, "isPlayerReady: $it, player: ${viewModel.player}")
                        binding.playerControlView.player = if (it) {
                            viewModel.player
                        } else {
                            null
                        }
                    }
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.currentSong.collectLatest {
                        Log.d(TAG, "song received: $it")
                        binding.root.isVisible = it != null
                        (it as? SongWrapper)?.let { wrapper -> renderSong(wrapper) }
                    }
                }
            }
        }
        binding.playerControlView.repeatToggleModes =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
        view.findViewById<ConstraintLayout>(R.id.controls_view).setOnClickListener {
            toFullScreen()
        }
    }

    private fun renderSong(song: SongWrapper) {
        view?.findViewById<TextView>(R.id.control_track_artist)?.text = song.artists.joinArtists()
        view?.findViewById<TextView>(R.id.control_track_title)?.text = song.title
        view?.findViewById<ShapeableImageView>(R.id.control_song_icon)?.let {
            Glide.with(requireActivity())
                .load(song)
                .placeholder(R.drawable.song_icon_preview)
                .into(it)
        }
    }

    private fun toFullScreen() {
        val options = ActivityOptions.makeSceneTransitionAnimation(
            requireActivity(),
            Pair.create(
                view?.findViewById<ShapeableImageView>(R.id.control_song_icon),
                context?.getString(R.string.player_transition)
            )
        )
        val bundle = options.toBundle()

        val intent = Intent(requireActivity(), FullScreenPlayerActivity::class.java)
        requireActivity().startActivity(intent, bundle)
    }

    override fun onDestroyView() {
        binding.playerControlView.player = null
        super.onDestroyView()
    }
}
