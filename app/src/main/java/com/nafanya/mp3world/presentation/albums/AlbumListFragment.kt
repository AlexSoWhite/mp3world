package com.nafanya.mp3world.presentation.albums

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
import com.nafanya.mp3world.domain.albums.Album
import com.nafanya.mp3world.presentation.albums.recycler.AlbumListAdapter
import com.nafanya.mp3world.presentation.albums.recycler.AlbumListItem

class AlbumListFragment : StatedListFragmentBaseLayout<Album, AlbumListItem>() {

    private val albumListAdapter: AlbumListAdapter by lazy {
        AlbumListAdapter {
            val starter = ActivityStarter.Builder()
                .with(requireActivity())
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
        viewModel.title.observe(viewLifecycleOwner) {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
