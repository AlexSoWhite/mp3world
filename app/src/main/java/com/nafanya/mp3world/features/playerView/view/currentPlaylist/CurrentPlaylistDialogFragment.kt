package com.nafanya.mp3world.features.playerView.view.currentPlaylist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nafanya.mp3world.core.di.PlayerApplication
import com.nafanya.mp3world.core.viewModel.ViewModelFactory
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import com.nafanya.mp3world.core.wrappers.remote.RemoteSong
import com.nafanya.mp3world.databinding.FragmentCurrentPlaylistDialogBinding
import com.nafanya.mp3world.features.downloading.DownloadViewModel
import com.nafanya.mp3world.features.favorites.viewModel.FavouriteListViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.SONG_LOCAL_IMMUTABLE
import com.nafanya.mp3world.features.songListViews.SONG_REMOTE
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog
import com.nafanya.mp3world.features.songListViews.actionDialogs.RemoteSongActionDialog
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.baseViews.SongView
import com.nafanya.mp3world.features.songListViews.songViews.ImmutableLocalSongViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RemoteSongViewHolder
import dagger.Lazy
import javax.inject.Inject

class CurrentPlaylistDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var favouriteListViewModel: Lazy<FavouriteListViewModel>

    @Inject
    lateinit var downloadViewModel: Lazy<DownloadViewModel>

    companion object {
        const val MAX_DIALOG_SIZE = 0.7f
    }

    private val viewModel: CurrentPlaylistViewModel by viewModels { factory }

    private val mixedAdapter: BaseSongListAdapter = object : BaseSongListAdapter() {

        override fun onBindViewHolder(holder: SongListItemViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val song = currentList[position].getDataAsSong()
            when (holder.itemViewType) {
                SONG_LOCAL_IMMUTABLE -> {
                    (holder as ImmutableLocalSongViewHolder).bind(
                        song,
                        onClickCallBack = { viewModel.onSongClick(song) },
                        onActionClickedCallback = {
                            val dialog = LocalSongActionDialog(
                                requireActivity(),
                                song as LocalSong,
                                favouriteListViewModel.get()
                            )
                            dialog.show()
                        }
                    )
                }
                SONG_REMOTE -> {
                    (holder as RemoteSongViewHolder).bind(
                        song,
                        onClickCallBack = { viewModel.onSongClick(song) },
                        onActionClickedCallback = {
                            val dialog = RemoteSongActionDialog(
                                requireActivity(),
                                song as RemoteSong,
                                downloadViewModel.get()
                            )
                            dialog.show()
                        }
                    )
                }
            }
        }
    }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currentPlaylistRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mixedAdapter
        }
        mixedAdapter.onBound.observe(viewLifecycleOwner) {
            viewModel.onFirstItemBound()
        }
        viewModel.listItems.observe(viewLifecycleOwner) {
            mixedAdapter.submitList(it)
        }
        viewModel.playlist.observe(viewLifecycleOwner) {
            binding.currentPlaylistTitle.text = it?.name
        }
        viewModel.currentSong.observe(viewLifecycleOwner) { song ->
            binding.currentPlaylistRecycler.allViews.filter {
                it is SongView
            }.forEach {
                if ((it as SongView).updateCurrentSong(song as SongWrapper)) {
                    currentPlayingView = it
                }
            }
        }
        viewModel.isPlaying.observe(viewLifecycleOwner) {
            currentPlayingView?.updateIsPlaying(it)
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
