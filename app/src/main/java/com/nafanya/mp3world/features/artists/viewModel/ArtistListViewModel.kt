package com.nafanya.mp3world.features.artists.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.title.TitleProcessor
import com.nafanya.mp3world.core.listUtils.title.TitleViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.mp3world.features.artists.view.recycler.ARTIST
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class ArtistListViewModel @Inject constructor(
    artistListManager: ArtistListManager
) : StatedListViewModel<Artist, ArtistListItem>(),
    Searchable<Artist>,
    TitleViewModel<List<Artist>> {

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
