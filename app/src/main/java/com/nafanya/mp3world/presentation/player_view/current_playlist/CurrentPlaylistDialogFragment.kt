package com.nafanya.mp3world.presentation.player_view.current_playlist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.databinding.FragmentCurrentPlaylistDialogBinding
import com.nafanya.mp3world.data.downloading.api.download
import com.nafanya.mp3world.presentation.song_list_views.action_dialogs.defaultLocalSongActionDialog
import com.nafanya.mp3world.presentation.song_list_views.base_views.SongView
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CurrentPlaylistDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var playerInteractor: PlayerInteractor

    companion object {
        const val MAX_DIALOG_SIZE = 0.7f
    }

    private val viewModel: CurrentPlaylistViewModel by viewModels { factory }

    private val mixedAdapter = MixedAdapter()

    private var mBinding: FragmentCurrentPlaylistDialogBinding? = null
    val binding
        get() = mBinding!!

    private var currentPlayingView: SongView? = null

    override fun onAttach(context: Context) {
        (context.applicationContext as PlayerApplication)
            .applicationComponent
            .playerViewComponent
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCurrentPlaylistDialogBinding.inflate(
            inflater,
            container,
            false
        )
        binding.root.maxHeight = (resources.displayMetrics.heightPixels * MAX_DIALOG_SIZE).toInt()
        binding.root.minHeight = (resources.displayMetrics.heightPixels * MAX_DIALOG_SIZE).toInt()
        viewModel.init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mixedAdapter.apply {
            onClickCallback = { view, song ->
                viewModel.onSongClick(song)
                currentPlayingView = view
            }
            onLocalActionClickCallback = (requireActivity() as AppCompatActivity)
                .defaultLocalSongActionDialog(viewModel)
            onRemoteActionClickCallback = { song ->
                download(viewModel, song)
            }
        }
        binding.currentPlaylistRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mixedAdapter
        }
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                val playlistWrapper = viewModel.playlistFlow.first()
                mixedAdapter.submitList(viewModel.asListItems(playlistWrapper.songList))
                binding.currentPlaylistTitle.text = playlistWrapper.name
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.currentSong.filterNotNull().collectLatest { song ->
                        // remove indicator from old view
                        currentPlayingView?.updateCurrentSong(song)
                        binding.currentPlaylistRecycler.allViews.filter {
                            it is SongView
                        }.forEach { view ->
                            with(view as SongView) {
                                if (updateCurrentSong(song)) {
                                    currentPlayingView = this
                                }
                            }
                        }
                        mixedAdapter.setCurrentPlayingSong(song)
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.isPlaying.collectLatest {
                        currentPlayingView?.updateIsPlaying(it)
                    }
                }
            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    mixedAdapter.currentSelectedView.collectLatest {
                        currentPlayingView = it
                        currentPlayingView?.updateIsPlaying(viewModel.isPlaying.value)
                    }
                }
            }
        }

        binding.dismissCurrentPlaylistDialog.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        dialog.window?.navigationBarColor = Color.parseColor("#373232")
        return dialog
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}
