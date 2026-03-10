package com.nafanya.mp3world.presentation.artists

import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.title.TitleModel
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.domain.artists.Artist
import com.nafanya.mp3world.domain.artists.ArtistPlaylistProvider
import com.nafanya.mp3world.presentation.artists.recycler.ARTIST
import com.nafanya.mp3world.presentation.artists.recycler.ArtistListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class ArtistListViewModel @Inject constructor(
    artistListProvider: ArtistPlaylistProvider
) : StatedListViewModel<Artist, ArtistListItem>(),
    Searchable<Artist>,
    TitleProcessorWrapper<List<Artist>> {

    private val searchProcessor = SearchProcessor<Artist>(
        QueryFilter { artist, query ->
            artist.name.contains(query, true)
        }
    )

    private val titleProcessor = TitleProcessor<List<Artist>>(
        baseTitleRes = R.string.artists
    )
    override val title: StateFlow<TitleModel>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            searchProcessor.setup(
                this,
                artistListProvider.artists.map { Data.Success(it) }
            )
        }
    }

    override fun asListItems(list: List<Artist>): List<ArtistListItem> {
        return list.map { ArtistListItem(ARTIST, it) }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }
}
