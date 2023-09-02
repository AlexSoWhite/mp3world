package com.nafanya.mp3world.features.dragging
/*
import com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist.MutablePlaylistActivity
import com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist.MutablePlaylistAdapter

package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.searching.SearchableFragment
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModel
import com.nafanya.mp3world.core.viewModel.StatePlaylistViewModelDraggable
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.songListViews.songViews.RearrangeableSongView
import com.nafanya.mp3world.features.allPlaylists.viewModel.MutablePlaylistViewModel
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.playlist.baseViews.StatePlaylistHolderFragment
import com.nafanya.mp3world.features.playlist.baseViews.StatePlaylistHolderFragmentDraggable
import com.nafanya.mp3world.features.songListViews.actionDialogs.LocalSongActionDialog
import com.nafanya.mp3world.features.songListViews.topButtonViews.ConfirmChangesButtonViewHolder
import javax.inject.Inject

class MutablePlaylistFragment :
    StatePlaylistHolderFragment(),
    SearchableFragment<SongWrapper> {

    @Inject
    lateinit var mutableFactory: MutablePlaylistViewModel.Factory.MutablePlaylistAssistedFactory

    private val viewModel: MutablePlaylistViewModel by viewModels {
        mutableFactory.create(
            arguments?.getLong(MutablePlaylistActivity.PLAYLIST_ID, -1) ?: -1
        )
    }
    override val playlistViewModel: StatePlaylistViewModel
        get() = viewModel

    private val mutablePlaylistAdapter: MutablePlaylistAdapter by lazy {
        MutablePlaylistAdapter(
            onSongClickCallback = {
                viewModel.onSongClick(it)
            },
            onModifyButtonClickCallback = {
                viewModel.playlist.value?.let {
                    ActivityStarter.Builder()
                        .with(requireContext())
                        .createIntentToModifyPlaylistActivity(it)
                        .build()
                        .startActivity()
                }
            },
            onLongPressCallback = {
                viewModel.moveToDraggableState()
            },
            onConfirmChangesCallback = {
                viewModel.confirmChanges()
            },
            onActionClickCallback = {
                val dialog = LocalSongActionDialog(
                    requireActivity(),
                    it,
                    favouriteListViewModel.get()
                )
                dialog.show()
            }
        )
    }

    override val playlistAdapter: BaseSongListAdapter
        get() = mutablePlaylistAdapter

    override val emptyMockImageResource: Int
        get() = R.drawable.playlist_add
    override val emptyMockTextResource: Int
        get() = R.string.add_songs

    private var onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.moveFromDraggableState()
        }
    }

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allPlaylistsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emptyMock.root.setOnClickListener {
            viewModel.playlist.value?.let {
                ActivityStarter
                    .Builder()
                    .with(requireContext())
                    .createIntentToModifyPlaylistActivity(it)
                    .build()
                    .startActivity()
            }
        }
    }

    override fun onMoved(startPosition: Int, endPosition: Int) {
        if (endPosition != 0) {
            adapter.notifyItemMoved(startPosition, endPosition)
            val song1 = adapter.currentList[startPosition].getDataAsSong()
            val song2 = adapter.currentList[endPosition].getDataAsSong()
            viewModel.rearrangeSongs(song1, song2)
        }
    }

    override fun getMovementFlagsOverride(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return when (viewHolder) {
            is ConfirmChangesButtonViewHolder -> {
                ItemTouchHelper.Callback.makeMovementFlags(0, 0)
            }
            else -> {
                super.getMovementFlagsOverride(recyclerView, viewHolder)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (viewModel.inDraggableMode.value == true) {
            inflater.inflate(R.menu.modify_playlist_menu, menu)
            val dismissButton = menu.findItem(R.id.dismiss_changes)
            dismissButton.setOnMenuItemClickListener {
                moveFromDraggableState()
                true
            }
        } else {
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun moveToDraggableState() {
        binding.recycler.allViews.forEach { item ->
            if (item is RearrangeableSongView) {
                item.moveToDraggableState { song ->
                    viewModel.removeSong(song)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()
    }

    override fun moveFromDraggableState() {
        binding.recycler.allViews.forEach { item ->
            if (item is RearrangeableSongView) {
                item.moveFromDraggableState()
            }
        }
        onBackPressedCallback.isEnabled = false
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()
    }
}


package com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist

import com.nafanya.mp3world.features.songListViews.topButtonViews.ModifyPlaylistButtonViewHolder
import com.nafanya.mp3world.features.playlist.baseViews.BaseSongListAdapter
import com.nafanya.mp3world.features.songListViews.CONFIRM_CHANGES_BUTTON
import com.nafanya.mp3world.features.songListViews.MODIFY_PLAYLIST_BUTTON
import com.nafanya.mp3world.features.songListViews.SONG_REARRANGEABLE
import com.nafanya.mp3world.features.songListViews.baseViews.SongListItemViewHolder
import com.nafanya.mp3world.features.songListViews.songViews.RearrangeableSongViewHolder
import com.nafanya.mp3world.features.songListViews.topButtonViews.ConfirmChangesButtonViewHolder
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.core.wrappers.LocalSong

class MutablePlaylistAdapter(
    private val onSongClickCallback: (SongWrapper) -> Unit,
    private val onModifyButtonClickCallback: () -> Unit,
    private val onLongPressCallback: (SongWrapper) -> Unit,
    private val onConfirmChangesCallback: () -> Unit,
    private val onActionClickCallback: (LocalSong) -> Unit
) : BaseSongListAdapter() {

    override fun onBindViewHolder(
        holder: SongListItemViewHolder,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        when (holder.itemViewType) {
            SONG_REARRANGEABLE -> {
                with(holder as RearrangeableSongViewHolder) {
                    val song = currentList[position].getDataAsSong()
                    bind(
                        song,
                        onClickCallBack =  { onSongClickCallback(song) },
                        onActionClickedCallback = { onActionClickCallback(song as LocalSong) }
                    )
                    holder.attachToScreen(onLongPressCallback)
                }
            }
            MODIFY_PLAYLIST_BUTTON -> {
                with(holder as ModifyPlaylistButtonViewHolder) {
                    bind { onModifyButtonClickCallback() }
                }
            }
            CONFIRM_CHANGES_BUTTON -> {
                with(holder as ConfirmChangesButtonViewHolder) {
                    bind { onConfirmChangesCallback() }
                }
            }
        }
    }
}

*/
