package com.nafanya.mp3world.features.artists.viewModel

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.core.listUtils.searching.StatedQueryFilter
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
    private val artistListManager: ArtistListManager
) : StatedListViewModel<Artist, ArtistListItem>(),
    SearchableStated<Artist>,
    TitleViewModel<List<Artist>> {

    override val queryFilter: StatedQueryFilter<Artist> = StatedQueryFilter { artist, query ->
        artist.name.contains(query, true)
    }

    override val baseTitle = "Исполнители"
    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (suspend (State<List<Artist>>) -> State<List<Artist>>)? = null

    init {
        model.load {
            viewModelScope.launch {
                setDataSourceFiltered(artistListManager.artists.map { Data.Success(it) }.asFlow())
                model.startListeningModelForTitle()
            }
        }
    }

    override fun asListItems(list: List<Artist>): List<ArtistListItem> {
        return list.map { ArtistListItem(ARTIST, it) }
    }
}
