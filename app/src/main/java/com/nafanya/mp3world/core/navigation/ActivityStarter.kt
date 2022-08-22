package com.nafanya.mp3world.core.navigation

import android.content.Context
import android.content.Intent
import com.nafanya.mp3world.core.listManagers.ALBUM_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.ARTIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.FAVOURITE_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.listManagers.PLAYLIST_LIST_MANAGER_KEY
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.features.albums.view.AlbumListActivity
import com.nafanya.mp3world.features.allPlaylists.view.allPlaylists.AllPlaylistsActivity
import com.nafanya.mp3world.features.allPlaylists.view.modifyPlaylist.ModifyPlaylistActivity
import com.nafanya.mp3world.features.allPlaylists.view.mutablePlaylist.MutablePlaylistActivity
import com.nafanya.mp3world.features.allSongs.view.AllSongsActivity
import com.nafanya.mp3world.features.artists.view.ArtistListActivity
import com.nafanya.mp3world.features.playlist.immutablePlaylist.ImmutablePlaylistActivity
import com.nafanya.mp3world.features.remoteSongs.view.RemoteSongsActivity
import com.nafanya.mp3world.features.remoteSongs.view.RemoteSongsActivity.Companion.QUERY

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
            return this
        }

        fun createIntentToModifyPlaylistActivity(playlist: PlaylistWrapper): Builder {
            intent = Intent(context, ModifyPlaylistActivity::class.java)
            intent.putExtra(ModifyPlaylistActivity.PLAYLIST_ID, playlist.id)
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
            return this
        }

        fun createIntentToImmutablePlaylistActivityFromAlbum(id: Long): Builder {
            intent = Intent(context, ImmutablePlaylistActivity::class.java)
            intent.putExtra(ImmutablePlaylistActivity.LIST_MANAGER_KEY, ALBUM_LIST_MANAGER_KEY)
            intent.putExtra(ImmutablePlaylistActivity.CONTAINER_ID, id)
            return this
        }

        fun createIntentToImmutablePlaylistActivityFromArtist(id: Long): Builder {
            intent = Intent(context, ImmutablePlaylistActivity::class.java)
            intent.putExtra(ImmutablePlaylistActivity.LIST_MANAGER_KEY, ARTIST_LIST_MANAGER_KEY)
            intent.putExtra(ImmutablePlaylistActivity.CONTAINER_ID, id)
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
