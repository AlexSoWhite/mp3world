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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.databinding.BottomControlViewBinding
import com.nafanya.player.Song
import javax.inject.Inject

class BottomControlView : Fragment() {

    private lateinit var binding: BottomControlViewBinding
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PlayerViewModel
    private lateinit var currentSong: Song

    override fun onAttach(context: Context) {
        (requireActivity().application as PlayerApplication)
            .applicationComponent
            .playerViewComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomControlViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = factory.create(PlayerViewModel::class.java)
        binding.playerControlView.showTimeoutMs = 0
        viewModel.isPlayerInitialised.observe(requireActivity()) {
            binding.playerControlView.player = viewModel.player
        }
        viewModel.currentSong.observe(requireActivity()) {
            currentSong = it
            renderSong(it)
        }
        binding.playerControlView.repeatToggleModes =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE or
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
        requireActivity().findViewById<LinearLayout>(R.id.controls_view).setOnClickListener {
            toFullScreen()
        }
    }

    private fun renderSong(song: Song) {
        requireActivity().findViewById<TextView>(R.id.control_track_artist).text = song.artist
        requireActivity().findViewById<TextView>(R.id.control_track_title).text = song.title
        setSongIcon()
    }

    private fun toFullScreen() {
        var bundle: Bundle? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                activity,
                Pair.create(
                    requireActivity().findViewById(R.id.control_song_icon),
                    context?.getString(R.string.player_transition)
                )
            )
            bundle = options.toBundle()
        }

        val intent = Intent(activity, FullScreenPlayerActivity::class.java)
        ContextCompat.startActivity(requireContext(), intent, bundle)
        // TODO for what purpose
        setSongIcon()
    }

    private fun setSongIcon() {
        val songIcon = requireActivity().findViewById<ImageView>(R.id.control_song_icon)
        when {
            currentSong.art != null -> {
                songIcon.setImageBitmap(currentSong.art)
            }
            currentSong.artUrl != null -> {
                Glide.with(songIcon)
                    .asBitmap()
                    .load(currentSong.artUrl)
                    .into(songIcon)
            }
            else -> {
                songIcon.setImageResource(R.drawable.music_menu_icon)
            }
        }
    }
}
