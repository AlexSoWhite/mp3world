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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.exoplayer2.util.RepeatModeUtil
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.mediaStore.MediaStoreReader
import com.nafanya.mp3world.core.view.BaseFragment
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.databinding.BottomControlViewBinding
import javax.inject.Inject

class BottomControlViewFragment : BaseFragment<BottomControlViewBinding>() {

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var mediaStoreReader: MediaStoreReader
    private lateinit var viewModel: PlayerViewModel
    private lateinit var currentSong: SongWrapper

    override fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ): BottomControlViewBinding {
        return BottomControlViewBinding.inflate(inflater, parent, attachToParent)
    }

    override fun onAttach(context: Context) {
        (requireActivity().application as PlayerApplication)
            .applicationComponent
            .playerViewComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = factory.create(PlayerViewModel::class.java)
        binding.playerControlView.showTimeoutMs = 0
        viewModel.isPlayerInitialised.observe(viewLifecycleOwner) {
            binding.playerControlView.player = viewModel.player
        }
        viewModel.currentSong.observe(viewLifecycleOwner) {
            binding.root.isVisible = true
            currentSong = viewModel.currentSong.value as SongWrapper
            renderSong(currentSong)
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

        val intent = Intent(requireActivity(), FullScreenPlayerActivity::class.java)
        requireActivity().startActivity(intent, bundle)
    }

    private fun setSongIcon() {
        val songIcon = view?.findViewById<ImageView>(R.id.control_song_icon)
        songIcon?.setImageBitmap(currentSong.art)
    }
}
