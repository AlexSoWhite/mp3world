package com.nafanya.mp3world.presentation.user_playlists.view_playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder
import com.nafanya.mp3world.core.utils.list_utils.searching.attachToTopBar
import com.nafanya.mp3world.presentation.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.recycler.AllPlaylistsAdapter
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.recycler.AllPlaylistsListItem
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.recycler.ClickType

class AllPlaylistsFragment : StatedListFragmentBaseLayout<PlaylistWrapper, AllPlaylistsListItem>() {

    private val viewModel: AllPlaylistsViewModel by viewModels { factory.get() }

    private val allPlaylistsAdapter: AllPlaylistsAdapter by lazy {
        AllPlaylistsAdapter(
            onAddPlaylistClickCallback = {
                (requireActivity() as AppCompatActivity)
                    .createAddPlaylistDialog {
                        viewModel.addEmptyPlaylistWithName(it)
                    }.show()
            },
            onPlaylistClickCallback = { playlist, clickType ->
                when (clickType) {
                    ClickType.CLICK -> {
                        ActivityStarter.Builder()
                            .with(requireContext())
                            .createIntentToMutablePlaylistActivity(playlist)
                            .build()
                            .startActivity()
                    }
                    ClickType.LONG -> {
                        (requireActivity() as AppCompatActivity)
                            .createDeletePlaylistDialog(playlist.name) {
                                viewModel.deletePlaylist(playlist)
                            }.show()
                    }
                }
            }
        )
    }

    override val adapter: BaseAdapter<AllPlaylistsListItem, out BaseViewHolder>
        get() = allPlaylistsAdapter
    override val listViewModel: StatedListViewModel<PlaylistWrapper, AllPlaylistsListItem>
        get() = viewModel
    override val emptyMockImageResource: Int
        get() = R.drawable.playlist_add
    override val emptyMockTextResource: Int
        get() = R.string.add_playlist

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.allPlaylistsComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emptyMock.root.setOnClickListener {
            (requireActivity() as AppCompatActivity)
                .createAddPlaylistDialog {
                    viewModel.addEmptyPlaylistWithName(it)
                }.show()
        }
        viewModel.title.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
