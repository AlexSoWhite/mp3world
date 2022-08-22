package com.nafanya.mp3world.features.allPlaylists.view.allPlaylists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.StateFragment
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.listUtils.searching.SearchableFragment
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsAdapter
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.AllPlaylistsListItem
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.recycler.ClickType
import com.nafanya.mp3world.features.allPlaylists.viewModel.AllPlaylistsViewModel

class AllPlaylistsFragment :
    StateFragment<PlaylistWrapper, AllPlaylistsListItem>(),
    SearchableFragment<PlaylistWrapper> {

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
    override val stateMachine: StateMachine<PlaylistWrapper, AllPlaylistsListItem>
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createTopBar(viewModel).invoke(menu, inflater)
    }
}