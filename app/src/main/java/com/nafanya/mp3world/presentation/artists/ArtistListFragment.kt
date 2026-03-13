package com.nafanya.mp3world.presentation.artists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.di.ApplicationComponent
import com.nafanya.mp3world.core.utils.list_utils.recycler.BaseAdapter
import com.nafanya.mp3world.core.utils.list_utils.recycler.view.BaseViewHolder
import com.nafanya.mp3world.core.utils.list_utils.searching.attachToTopBar
import com.nafanya.mp3world.presentation.core.navigation.ActivityStarter
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListFragmentBaseLayout
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.domain.artists.Artist
import com.nafanya.mp3world.presentation.artists.recycler.ArtistListAdapter
import com.nafanya.mp3world.presentation.artists.recycler.ArtistListItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.title.collectLatest {
                    val activity = requireActivity() as AppCompatActivity
                    activity.supportActionBar?.title = it.getText(activity)
                }
            }
        }
    }

    // todo(New API)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.attachToTopBar().invoke(menu, inflater)
    }
}
