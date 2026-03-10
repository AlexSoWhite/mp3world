package com.nafanya.mp3world.presentation.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.state_machines.presentation.Data
import com.nafanya.mp3world.core.state_machines.presentation.list.StatedListViewModel
import com.nafanya.mp3world.core.utils.list_utils.searching.QueryFilter
import com.nafanya.mp3world.core.utils.list_utils.searching.SearchProcessor
import com.nafanya.mp3world.core.utils.list_utils.searching.Searchable
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessor
import com.nafanya.mp3world.core.utils.list_utils.title.TitleProcessorWrapper
import com.nafanya.mp3world.domain.albums.Album
import com.nafanya.mp3world.domain.albums.AlbumPlaylistProvider
import com.nafanya.mp3world.presentation.albums.recycler.ALBUM
import com.nafanya.mp3world.presentation.albums.recycler.AlbumListItem
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class AlbumListViewModel @Inject constructor(
    albumListManager: AlbumPlaylistProvider
) : StatedListViewModel<Album, AlbumListItem>(),
    Searchable<Album>,
    TitleProcessorWrapper<List<Album>> {

    private val searchProcessor = SearchProcessor<Album>(
        QueryFilter { album, query ->
            album.name.contains(query, true)
        }
    )

    private val titleProcessor = TitleProcessor<List<Album>>()
    override val title: LiveData<String>
        get() = titleProcessor.title

    init {
        model.load {
            titleProcessor.setup(model, viewModelScope)
            // TODO: string resource
            titleProcessor.setBaseTitle("Мои альбомы")
            searchProcessor.setup(
                this,
                albumListManager.albums.map { Data.Success(it) }
            )
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
