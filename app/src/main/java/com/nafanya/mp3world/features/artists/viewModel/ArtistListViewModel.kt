package com.nafanya.mp3world.features.artists.viewModel

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.stateMachines.State
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.core.stateMachines.title.TitleViewModel
import com.nafanya.mp3world.features.artists.Artist
import com.nafanya.mp3world.features.artists.ArtistListManager
import com.nafanya.mp3world.features.artists.view.recycler.ARTIST
import com.nafanya.mp3world.features.artists.view.recycler.ArtistListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ArtistListViewModel @Inject constructor(
    artistListManager: ArtistListManager
) : StatedListViewModel<Artist, ArtistListItem>(),
    Searchable<Artist>,
    TitleViewModel<List<Artist>> {

    override val baseTitle = "Исполнители"
    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (suspend (State<List<Artist>>) -> State<List<Artist>>)? = null

    private val searchProcessor = SearchProcessor<Artist>(
        QueryFilter { artist, query ->
            artist.name.contains(query, true)
        }
    )

    init {
        model.load {
            viewModelScope.launch {
                searchProcessor.setup(
                    this@ArtistListViewModel,
                    artistListManager.artists.map { Data.Success(it) }.asFlow()
                )
                model.startListeningModelForTitle()
            }
        }
    }

    override fun asListItems(list: List<Artist>): List<ArtistListItem> {
        return list.map { ArtistListItem(ARTIST, it) }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }
}
