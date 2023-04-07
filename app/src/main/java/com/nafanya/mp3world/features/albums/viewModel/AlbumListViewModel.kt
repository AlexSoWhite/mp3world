package com.nafanya.mp3world.features.albums.viewModel

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.SearchableStated
import com.nafanya.mp3world.core.listUtils.searching.StatedQueryFilter
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AlbumListViewModel @Inject constructor(
    albumListManager: AlbumListManager
) : StatedListViewModel<Album, AlbumListItem>(),
    SearchableStated<Album>,
    TitleViewModel<List<Album>> {

    override val queryFilter: StatedQueryFilter<Album> = StatedQueryFilter { album, query ->
        album.name.contains(query, true)
    }

    override fun asListItems(list: List<Album>): List<AlbumListItem> {
        return list.map {
            AlbumListItem(ALBUM, it)
        }
    }

    override val baseTitle = "Мои альбомы"
    override val mTitle = MutableStateFlow(baseTitle)

    override val stateMapper: (suspend (State<List<Album>>) -> State<List<Album>>)? = null

    init {
        model.load {
            viewModelScope.launch {
                setDataSourceFiltered(
                    albumListManager.albums.asFlow().map {
                        Data.Success(it)
                    }
                )
                model.startListeningModelForTitle()
            }
        }
    }
}
