package com.nafanya.mp3world.features.playerView.view

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.google.android.material.imageview.ShapeableImageView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.view.BaseFragment
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.databinding.PlayerControlViewBottomFragmentBinding
import javax.inject.Inject

class BottomControlViewFragment : BaseFragment<PlayerControlViewBottomFragmentBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory

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
        lifecycleScope.launchWhenCreated {
            viewModel.isPlayerInitialised.collect {
                if (it) {
                    binding.playerControlView.player = viewModel.player
                } else {
                    binding.playerControlView.player = null
                }
            }
        }
        viewModel.currentSong.observe(viewLifecycleOwner) {
            binding.root.isVisible = true
            renderSong(it as SongWrapper)
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
        view?.findViewById<TextView>(R.id.control_track_artist)?.text = song.artist
        view?.findViewById<TextView>(R.id.control_track_title)?.text = song.title
        view?.findViewById<ShapeableImageView>(R.id.control_song_icon)?.setImageBitmap(song.art)
    }

    private fun toFullScreen() {
        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                Pair.create(
                    view?.findViewById<ShapeableImageView>(R.id.control_song_icon),
                    context?.getString(R.string.player_transition)
                )
            )
            bundle = options.toBundle()
        }

        val intent = Intent(requireActivity(), FullScreenPlayerActivity::class.java)
        requireActivity().startActivity(intent, bundle)
    }

    override fun onDestroyView() {
        binding.playerControlView.player = null
        super.onDestroyView()
    }
}
