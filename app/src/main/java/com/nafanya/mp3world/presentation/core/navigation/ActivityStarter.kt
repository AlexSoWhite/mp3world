package com.nafanya.mp3world.presentation.core.navigation

import android.content.Context
import android.content.Intent
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.list_managers.ALBUM_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.ARTIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.list_managers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.wrappers.playlist.PlaylistWrapper
import com.nafanya.mp3world.presentation.albums.AlbumListActivity
import com.nafanya.mp3world.presentation.user_playlists.view_playlists.AllPlaylistsActivity
import com.nafanya.mp3world.presentation.user_playlists.modify_playlist.ModifyPlaylistActivity
import com.nafanya.mp3world.presentation.user_playlists.mutable_playlist.MutablePlaylistActivity
import com.nafanya.mp3world.presentation.all_songs.AllSongsActivity
import com.nafanya.mp3world.presentation.artists.ArtistListActivity
import com.nafanya.mp3world.presentation.playlist.immutable_playlist.ImmutablePlaylistActivity
import com.nafanya.mp3world.presentation.playlist.immutable_playlist.ImmutablePlaylistActivity.Companion.PLAYLIST_NAME
import com.nafanya.mp3world.presentation.playlist.immutable_playlist.ImmutablePlaylistActivity.Companion.PLAYLIST_NAME_RES
import com.nafanya.mp3world.presentation.remote_songs.RemoteSongsActivity
import com.nafanya.mp3world.presentation.remote_songs.RemoteSongsActivity.Companion.QUERY

// todo: this is definitely a suboptimal approach
// todo: launching an activity for each screen is probably not the way
@Suppress("TooManyFunctions")
class ActivityStarter private constructor(
    private val context: Context,
    private val intent: Intent
) {

    class Builder {

        private lateinit var context: Context
        private lateinit var intent: Intent

        fun with(context: Context): Builder {
            this.context = context
            return this
        }

        fun createIntentToAllSongsActivity(): Builder {
            intent = Intent(context, AllSongsActivity::class.java)
            return this
        }

        fun createIntentToAllPlaylistsActivity(): Builder {
            intent = Intent(context, AllPlaylistsActivity::class.java)
            return this
        }

        fun createIntentToMutablePlaylistActivity(playlist: PlaylistWrapper): Builder {
            intent = Intent(context, MutablePlaylistActivity::class.java)
            intent.putExtra(MutablePlaylistActivity.PLAYLIST_ID, playlist.id)
            intent.putExtra(MutablePlaylistActivity.PLAYLIST_NAME, playlist.name)
            return this
        }

        fun createIntentToModifyPlaylistActivity(playlist: PlaylistWrapper): Builder {
            intent = Intent(context, ModifyPlaylistActivity::class.java)
            intent.putExtra(ModifyPlaylistActivity.PLAYLIST_ID, playlist.id)
            intent.putExtra(ModifyPlaylistActivity.PLAYLIST_NAME, playlist.name)
            return this
        }

        fun createIntentToArtistListActivity(): Builder {
            intent = Intent(context, ArtistListActivity::class.java)
            return this
        }

        fun createIntentToAlbumListActivity(): Builder {
            intent = Intent(context, AlbumListActivity::class.java)
            return this
        }

        fun createIntentToFavouritesActivity(): Builder {
            intent = Intent(context, ImmutablePlaylistActivity::class.java)
            intent.putExtra(ImmutablePlaylistActivity.LIST_MANAGER_KEY, FAVOURITE_LIST_MANAGER_KEY)
            intent.putExtra(PLAYLIST_NAME_RES, R.string.favourites)
            return this
        }

        fun createIntentToImmutablePlaylistActivityFromAlbum(id: Long, albumName: String): Builder {
            intent = Intent(context, ImmutablePlaylistActivity::class.java)
            intent.putExtra(ImmutablePlaylistActivity.LIST_MANAGER_KEY, ALBUM_LIST_MANAGER_KEY)
            intent.putExtra(ImmutablePlaylistActivity.CONTAINER_ID, id)
            intent.putExtra(PLAYLIST_NAME, albumName)
            return this
        }

        fun createIntentToImmutablePlaylistActivityFromArtist(
            id: Long,
            artistName: String
        ): Builder {
            intent = Intent(context, ImmutablePlaylistActivity::class.java)
            intent.putExtra(ImmutablePlaylistActivity.LIST_MANAGER_KEY, ARTIST_LIST_MANAGER_KEY)
            intent.putExtra(ImmutablePlaylistActivity.CONTAINER_ID, id)
            intent.putExtra(PLAYLIST_NAME, artistName)
            return this
        }

        fun createIntentToImmutablePlaylistActivity(playlist: PlaylistWrapper): Builder {
            intent = Intent(context, ImmutablePlaylistActivity::class.java)
            intent.putExtra(ImmutablePlaylistActivity.LIST_MANAGER_KEY, PLAYLIST_LIST_MANAGER_KEY)
            intent.putExtra(ImmutablePlaylistActivity.CONTAINER_ID, playlist.id)
            return this
        }

        fun createIntentToRemoteSongsActivity(query: String): Builder {
            intent = Intent(context, RemoteSongsActivity::class.java)
            intent.putExtra(QUERY, query)
            return this
        }

        fun build(): ActivityStarter {
            return ActivityStarter(context, intent)
        }
    }

    fun startActivity() {
        context.startActivity(intent)
    }
}
