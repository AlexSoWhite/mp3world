package com.nafanya.mp3world.features.albums.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.SearchProcessor
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.core.listUtils.title.TitleProcessor
import com.nafanya.mp3world.core.listUtils.title.TitleViewModel
import com.nafanya.mp3world.core.stateMachines.common.Data
import com.nafanya.mp3world.core.stateMachines.list.StatedListViewModel
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.mp3world.features.albums.view.recycler.ALBUM
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListItem
import javax.inject.Inject

class AlbumListViewModel @Inject constructor(
    albumListManager: AlbumListManager
) : StatedListViewModel<Album, AlbumListItem>(),
    Searchable<Album>,
    TitleViewModel<List<Album>> {

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
                albumListManager.albums.map { Data.Success(it) }.asFlow()
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
