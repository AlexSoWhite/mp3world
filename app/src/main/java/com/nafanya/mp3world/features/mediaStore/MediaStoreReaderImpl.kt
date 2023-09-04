package com.nafanya.mp3world.features.mediaStore

import android.content.Context
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.nafanya.mp3world.core.wrappers.song.UriFactory
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import javax.inject.Inject

/**
 * TODO: file observer
 */
class MediaStoreReaderImpl @Inject constructor(
    private val context: Context,
    private val uriFactory: UriFactory
) : MediaStoreReader {

    // get all the fields from media storage
    private val projection = null

    private val selection = null

    private val selectionArgs = null

    // sort based on date
    private val sortOrder = MediaStore.Audio.Media.DATE_MODIFIED

    @Suppress("LongMethod", "NestedBlockDepth")
    @WorkerThread
    override fun readMediaStore(): List<LocalSong> {
        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "$sortOrder DESC"
        )
        val list = mutableListOf<LocalSong>()
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
                        list.add(song)
                    }
                }
            }
        }
        return list
    }
}
