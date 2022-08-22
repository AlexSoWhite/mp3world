package com.nafanya.mp3world.features.albums.view

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.StateFragment
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.listUtils.searching.SearchableFragment
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListAdapter
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListItem
import com.nafanya.mp3world.features.albums.viewModel.AlbumListViewModel

class AlbumListFragment :
    StateFragment<Album, AlbumListItem>(),
    SearchableFragment<Album> {

    private val albumListAdapter: AlbumListAdapter by lazy {
        AlbumListAdapter {
            val starter = ActivityStarter.Builder()
                .with(this.requireActivity())
                .createIntentToImmutablePlaylistActivityFromAlbum(it.id)
                .build()
            starter.startActivity()
        }
    }
    override val adapter: BaseAdapter<AlbumListItem, out BaseViewHolder>
        get() = albumListAdapter

    private val viewModel: AlbumListViewModel by viewModels { factory.get() }
    override val stateMachine: StateMachine<Album, AlbumListItem>
        get() = viewModel

    override val emptyMockImageResource = R.drawable.album
    override val emptyMockTextResource = R.string.no_albums

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.albumComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createTopBar(viewModel).invoke(menu, inflater)
    }
}
