package com.nafanya.mp3world.presentation.artists

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.domain.artists.Artist
import com.nafanya.mp3world.domain.artists.ArtistPlaylistProvider
import com.nafanya.mp3world.presentation.artists.recycler.ARTIST
import com.nafanya.mp3world.presentation.artists.recycler.ArtistListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class ArtistListViewModel @Inject constructor(
    artistListManager: ArtistPlaylistProvider
) : StatedListViewModel<Artist, ArtistListItem>(),
    Searchable<Artist>,
    TitleProcessorWrapper<List<Artist>> {

    private val searchProcessor = SearchProcessor<Artist>(
        QueryFilter { artist, query ->
            artist.name.contains(query, true)
        }
    )

    private val titleProcessor = TitleProcessor<List<Artist>>()
    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            // TODO: string resource
            titleProcessor.setBaseTitle("Исполнители")
            searchProcessor.setup(
                this,
                artistListManager.artists.map { Data.Success(it) }
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
