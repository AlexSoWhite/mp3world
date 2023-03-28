package com.nafanya.mp3world.core.mediaStore

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.ArtFactory
import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.launch

@Suppress("LongMethod", "NestedBlockDepth")
/**
 * Class that reads local MediaStore.
 * @property allSongs holds all song objects that device has
 */
@Singleton
class MediaStoreReader @Inject constructor(
    private val context: Context,
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val artFactory: ArtFactory
) {

    companion object {
        private var isInitialized = false
    }

    private val mAllSongs = MutableLiveData<List<LocalSong>>()
    val allSongs: LiveData<List<LocalSong>>
        get() = mAllSongs

    /**
     * Sets managers data on main thread.
     */
    fun readMediaStore() {
        if (!isInitialized) {
            ioCoroutineProvider.ioScope.launch {
                initialize()
            }
        }
    }

    /**
     * Resets SongListManager and other managers data on background thread.
     */
    fun reset() {
        ioCoroutineProvider.ioScope.launch {
            initialize()
        }
    }

    private fun initialize() {
        // get all the fields from media storage
        val projection = null
        // select only music
        val selection = null
        val selectionArgs = null
        // sort based on date
        val sortOrder = MediaStore.Audio.Media.DATE_MODIFIED
        val allSongsList = mutableListOf<LocalSong>()
        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "$sortOrder DESC"
        )
        query?.use { cursor ->
            with(cursor) {
                val titleColumn = getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn = getColumnIndex(MediaStore.Audio.Media._ID)
                val artistColumn = getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val artistIdColumn = getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                val dateColumn = getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)
                val albumIdColumn = getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val albumColumn = getColumnIndex(MediaStore.Audio.Media.ALBUM)
                val durationColumn = getColumnIndex(MediaStore.Audio.Media.DURATION)
                while (moveToNext()) {
                    // Use an ID column from the projection to get
                    // a URI representing the media item itself.
                    val thisId = getLong(idColumn)
                    val thisTitle = getString(titleColumn)
                    val thisArtist = getString(artistColumn)
                    val thisDate = getLong(dateColumn)
                    val thisArtistId = getLong(artistIdColumn)
                    val thisAlbumId = getLong(albumIdColumn)
                    val thisAlbumName = getString(albumColumn)
                    val thisDuration = getLong(durationColumn)
                    // tracks with <unknown> artist are often corrupted
                    if (thisArtist != "<unknown>") {
                        // set the song art
                        // build song object
                        val thisUri = UriFactory().getUri(thisId)
                        val thisArt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            artFactory.createBitmap(thisUri)
                        } else {
                            artFactory.createBitmap(thisAlbumId)
                        }
                        val song = LocalSong(
                            uri = thisUri,
                            title = thisTitle,
                            artistId = thisArtistId,
                            artist = thisArtist ?: "unknown",
                            albumId = thisAlbumId,
                            album = thisAlbumName ?: "unknown",
                            date = thisDate,
                            duration = thisDuration,
                            art = thisArt
                        )
                        allSongsList.add(song)
                    }
                }
            }
        }
        mAllSongs.postValue(allSongsList)
        isInitialized = true
    }
}
