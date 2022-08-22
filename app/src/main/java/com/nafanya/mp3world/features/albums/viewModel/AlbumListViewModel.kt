package com.nafanya.mp3world.features.albums.viewModel

import androidx.lifecycle.map
import com.nafanya.mp3world.core.listUtils.StateMachine
import com.nafanya.mp3world.core.listUtils.searching.QueryFilter
import com.nafanya.mp3world.core.listUtils.searching.Searchable
import com.nafanya.mp3world.features.albums.Album
import com.nafanya.mp3world.features.albums.AlbumListManager
import com.nafanya.mp3world.features.albums.view.recycler.ALBUM
import com.nafanya.mp3world.features.albums.view.recycler.AlbumListItem
import javax.inject.Inject

class AlbumListViewModel @Inject constructor(
    albumListManager: AlbumListManager
) : StateMachine<Album, AlbumListItem>(
    albumListManager.albums.map { it }
),
    Searchable<Album> {

    override val filter: QueryFilter<Album> = QueryFilter { album, query ->
        album.name.contains(query, true)
    }

    override fun List<Album>.asListItems(): List<AlbumListItem> {
        return this.map {
            AlbumListItem(ALBUM, it)
        }
    }

    init {
        applyFilter(this)
        resetInitialTitle("Альбомы")
    }
}
