package com.nafanya.mp3world.core.mediaStore

import android.content.Context
import android.provider.MediaStore
import com.nafanya.mp3world.core.coroutines.IOCoroutineProvider
import com.nafanya.mp3world.core.wrappers.SongList
import com.nafanya.mp3world.core.wrappers.UriFactory
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

/**
 * TODO: file observer
 */
class MediaStoreReader @Inject constructor(
    private val context: Context,
    private val ioCoroutineProvider: IOCoroutineProvider,
    private val uriFactory: UriFactory
) {

    // get all the fields from media storage
    private val projection = null

    private val selection = null

    private val selectionArgs = null

    // sort based on date
    private val sortOrder = MediaStore.Audio.Media.DATE_MODIFIED

    // private val fileObserver: FileObserver

    private val mClosedSongList = SongList<LocalSong>()

    /**
     * Returns song flow
     */
    val songList: SharedFlow<List<LocalSong>?>
        get() = mClosedSongList.listFlow
            .map {
                it?.sortedByDescending { song -> song.date }
            }
            .shareIn(
                ioCoroutineProvider.ioScope,
                replay = 1,
                started = SharingStarted.Lazily
            )

    init {
        /*
        fileObserver = object : FileObserver(File(Environment.DIRECTORY_DOWNLOADS)) {
            override fun onEvent(event: Int, path: String?) {

            }
        }
        fileObserver.startWatching()
         */
    }

    @Suppress("LongMethod", "NestedBlockDepth")
    fun readMediaStore() {
        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "$sortOrder DESC"
        )
        mClosedSongList.setEmptyList()
        mClosedSongList.lock()
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
                        val thisUri = uriFactory.getUri(thisId)
                        val song = LocalSong(
                            uri = thisUri,
                            title = thisTitle,
                            artistId = thisArtistId,
                            artist = thisArtist ?: "unknown",
                            albumId = thisAlbumId,
                            album = thisAlbumName ?: "unknown",
                            date = thisDate,
                            duration = thisDuration
                        )
                        mClosedSongList.addOrUpdateSongWrapper(song)
                    }
                }
            }
            mClosedSongList.unlock()
        }
    }
}
