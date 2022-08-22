package com.nafanya.mp3world.features.artists.view

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
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListAdapter
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListItem
import com.nafanya.mp3world.features.artists.viewModel.ArtistListViewModel

class ArtistListFragment :
    StateFragment<Artist, ArtistListItem>(),
    SearchableFragment<Artist> {

    private val artistListAdapter: ArtistListAdapter by lazy {
        ArtistListAdapter {
            val starter = ActivityStarter.Builder()
                .with(this.requireActivity())
                .createIntentToImmutablePlaylistActivityFromArtist(it.id)
                .build()
            starter.startActivity()
        }
    }
    override val adapter: BaseAdapter<ArtistListItem, out BaseViewHolder>
        get() = artistListAdapter

    private val viewModel: ArtistListViewModel by viewModels { factory.get() }
    override val stateMachine: StateMachine<Artist, ArtistListItem>
        get() = viewModel

    override val emptyMockImageResource: Int
        get() = R.drawable.artist
    override val emptyMockTextResource: Int
        get() = R.string.no_artists

    override fun onInject(applicationComponent: ApplicationComponent) {
        applicationComponent.artistsComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createTopBar(viewModel).invoke(menu, inflater)
    }
}
