package com.nafanya.mp3world.features.artists.viewModel

import androidx.lifecycle.map
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.mp3world.features.artists.view.recycler.ARTIST
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListItem
import javax.inject.Inject

class ArtistListViewModel @Inject constructor(
    artistListManager: ArtistListManager
) : StateMachine<Artist, ArtistListItem>(
    artistListManager.artists.map { it },
),
    Searchable<Artist> {

    override val filter: QueryFilter<Artist> = QueryFilter { artist, query ->
        artist.name.contains(query, true)
    }

    init {
        applyFilter(this)
        resetInitialTitle("Исполнители")
    }

    override fun List<Artist>.asListItems(): List<ArtistListItem> {
        return this.map { ArtistListItem(ARTIST, it) }
    }
}
