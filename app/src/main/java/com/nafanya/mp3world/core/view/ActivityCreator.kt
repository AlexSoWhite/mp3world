package com.nafanya.mp3world.core.view

import android.content.Context
import android.content.Intent
import com.nafanya.mp3world.core.source.SourceProvider
import com.nafanya.mp3world.features.playlists.playlistsList.model.PlaylistListViewModelProvider
import com.nafanya.mp3world.features.playlists.playlist.model.PlaylistViewModelProvider
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.allSongs.SongListManager
import com.nafanya.mp3world.features.playlists.playlist.model.Playlist
import com.nafanya.mp3world.features.albums.view.AlbumListActivity
import com.nafanya.mp3world.features.artists.view.ArtistListActivity
import com.nafanya.mp3world.features.favorites.view.FavouriteListActivity
import com.nafanya.mp3world.features.playlists.playlist.view.AddSongToListActivity
import com.nafanya.mp3world.features.playlists.playlist.view.PlaylistActivity
import com.nafanya.mp3world.features.playlists.playlistsList.view.PlaylistListActivity
import com.nafanya.mp3world.features.searching.view.SearchSongListActivity
import com.nafanya.mp3world.features.playlists.playlistsList.viewModel.PlaylistListViewModel
import com.nafanya.mp3world.features.playlists.playlist.viewModel.PlaylistViewModel

// TODO consider refactoring
@Suppress("TooManyFunctions")
class ActivityCreator {

    private lateinit var context: Context
    private lateinit var intent: Intent

    fun with(context: Context): ActivityCreator {
        this.context = context
        return this
    }

    fun createSongListActivity(): ActivityCreator {
        intent = Intent(context, PlaylistActivity::class.java)
        intent.putExtra("isMutable", false)
        SourceProvider.putPlaylist(
            Playlist(
                name = "Мои песни",
                songList = SongListManager.songList.value ?: mutableListOf()
            )
        )
        return this
    }

    fun createPlaylistListActivity(): ActivityCreator {
        intent = Intent(context, PlaylistListActivity::class.java)
        return this
    }

    fun createPlaylistActivity(
        playlist: Playlist
    ): ActivityCreator {
        intent = Intent(context, PlaylistActivity::class.java)
        intent.putExtra("isMutable", false)
        SourceProvider.putPlaylist(playlist)
        return this
    }

    fun createMutablePlaylistActivity(
        playlist: Playlist,
        playlistListViewModel: PlaylistListViewModel
    ): ActivityCreator {
        intent = Intent(context, PlaylistActivity::class.java)
        intent.putExtra("isMutable", true)
        SourceProvider.putPlaylist(playlist)
        PlaylistListViewModelProvider.putPlaylistListViewModel(playlistListViewModel)
        return this
    }

    fun createAddSongToListActivity(
        playlistViewModel: PlaylistViewModel
    ): ActivityCreator {
        intent = Intent(context, AddSongToListActivity::class.java)
        SourceProvider.putPlaylist(
            Playlist(
                songList = SongListManager.songList.value ?: mutableListOf()
            )
        )
        PlaylistViewModelProvider.putPlaylistViewModel(playlistViewModel)
        return this
    }

    fun createArtistListActivity(): ActivityCreator {
        intent = Intent(context, ArtistListActivity::class.java)
        return this
    }

    fun createAlbumListActivity(): ActivityCreator {
        intent = Intent(context, AlbumListActivity::class.java)
        return this
    }

    fun createFavouriteListActivity(): ActivityCreator {
        intent = Intent(context, FavouriteListActivity::class.java)
        SourceProvider.putPlaylist(FavouriteListManager.favorites.value!!)
        return this
    }

    fun createSearchSongListActivity(
        query: String
    ): ActivityCreator {
        intent = Intent(context, SearchSongListActivity::class.java)
        SourceProvider.putQuery(query)
        return this
    }

    fun start() {
        context.startActivity(intent)
    }
}
