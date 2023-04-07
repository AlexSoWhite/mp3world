package com.nafanya.mp3world.features.albums.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.stateMachines.list.StatedListFragmentBaseLayout
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.utils.attachToTopBar
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListAdapter
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListItem
import com.nafanya.mp3world.features.albums.viewModel.AlbumListViewModel
import kotlinx.coroutines.flow.collectLatest

class AlbumListFragment : StatedListFragmentBaseLayout<Album, AlbumListItem>() {

    private val albumListAdapter: AlbumListAdapter by lazy {
        AlbumListAdapter {
            val starter = ActivityStarter.Builder()
                .with(this.requireActivity())
                .createIntentToImmutablePlaylistActivityFromAlbum(it.id, it.name)
                .build()
            starter.startActivity()
        }
    }
    override val adapter: BaseAdapter<AlbumListItem, out BaseViewHolder>
        get() = albumListAdapter

    private val viewModel: AlbumListViewModel by viewModels { factory.get() }
    override val listViewModel: StatedListViewModel<Album, AlbumListItem>
        get() = viewModel

    override val emptyMockImageResource = R.drawable.icv_album
    override val emptyMockTextResource = R.string.no_albums

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.albumComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.title.collectLatest {
                (requireActivity() as AppCompatActivity).supportActionBar?.title = it
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
