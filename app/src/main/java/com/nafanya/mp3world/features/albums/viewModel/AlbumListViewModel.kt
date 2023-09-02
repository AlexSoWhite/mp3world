package com.nafanya.mp3world.features.albums.viewModel

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
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.mp3world.features.albums.view.recycler.ALBUM
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlbumListViewModel @Inject constructor(
    albumListManager: AlbumListManager
) : StatedListViewModel<Album, AlbumListItem>(),
    Searchable<Album>,
    TitleViewModel<List<Album>> {

    override val baseTitle = "Мои альбомы"
    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (suspend (State<List<Album>>) -> State<List<Album>>)? = null

    private val searchProcessor = SearchProcessor<Album>(
        QueryFilter { album, query ->
            album.name.contains(query, true)
        }
    )

    init {
        model.load {
            viewModelScope.launch {
                searchProcessor.setup(
                    this@AlbumListViewModel,
                    albumListManager.albums.map { Data.Success(it) }.asFlow()
                )
                model.startListeningModelForTitle()
            }
        }
    }

    override fun asListItems(list: List<Album>): List<AlbumListItem> {
        return list.map {
            AlbumListItem(ALBUM, it)
        }
    }

    override fun search(query: String) {
        searchProcessor.search(query)
    }
}
