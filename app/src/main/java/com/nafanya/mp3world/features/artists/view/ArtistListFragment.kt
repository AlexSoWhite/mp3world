package com.nafanya.mp3world.features.artists.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.listUtils.recycler.BaseAdapter
import com.nafanya.mp3world.core.listUtils.recycler.views.BaseViewHolder
import com.nafanya.mp3world.core.listUtils.searching.attachToTopBar
import com.nafanya.mp3world.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.stateMachines.commonUi.list.StatedListFragmentBaseLayout
import com.nafanya.mp3world.core.stateMachines.commonUi.list.StatedListViewModel
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListAdapter
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListItem
import com.nafanya.mp3world.features.artists.viewModel.ArtistListViewModel

class ArtistListFragment : StatedListFragmentBaseLayout<Artist, ArtistListItem>() {

    private val artistListAdapter: ArtistListAdapter by lazy {
        ArtistListAdapter {
            val starter = ActivityStarter.Builder()
                .with(this.requireActivity())
                .createIntentToImmutablePlaylistActivityFromArtist(it.id, it.name)
                .build()
            starter.startActivity()
        }
    }
    override val adapter: BaseAdapter<ArtistListItem, out BaseViewHolder>
        get() = artistListAdapter

    private val viewModel: ArtistListViewModel by viewModels { factory.get() }

    override val listViewModel: StatedListViewModel<Artist, ArtistListItem>
        get() = viewModel

    override val emptyMockImageResource: Int
        get() = R.drawable.icv_artist
    override val emptyMockTextResource: Int
        get() = R.string.no_artists

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.artistsComponent.inject(this)
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
