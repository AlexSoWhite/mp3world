package com.nafanya.mp3world.data.media_store

import android.content.Context
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.nafanya.mp3world.core.coroutines.DispatchersProvider
import com.nafanya.mp3world.core.wrappers.song.ArtistMetadata
import com.nafanya.mp3world.core.wrappers.song.UriFactory
import com.nafanya.mp3world.core.wrappers.song.local.LocalSong
import com.nafanya.mp3world.core.wrappers.song.splitArtistNames
import javax.inject.Inject
import kotlin.collections.forEach
import kotlinx.coroutines.withContext

/**
 * TODO: file observer
 */
class MediaStoreReaderImpl @Inject constructor(
    private val context: Context,
    private val uriFactory: UriFactory,
    private val dispatchersProvider: DispatchersProvider
) : MediaStoreReader {

    // get all the fields from media storage
    private val projection = null

    private val selection = null

    private val selectionArgs = null

    // sort based on date
    private val sortOrder = MediaStore.Audio.Media.DATE_MODIFIED

    private data class MediaArtistData(
        val id: Long,
        val originalName: String,
    )

    @Suppress("LongMethod", "NestedBlockDepth")
    @WorkerThread
    override suspend fun readMediaStore(): List<LocalSong> = withContext(dispatchersProvider.io) {
        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "$sortOrder ASC"
        )
        val list = mutableListOf<LocalSong>()
        query?.use { cursor ->
            with(cursor) {
                var localArtistId = 0L
                val localArtistsMap = mutableMapOf<String, MediaArtistData>()
                val titleColumn = getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn = getColumnIndex(MediaStore.Audio.Media._ID)
                val artistColumn = getColumnIndex(MediaStore.Audio.Media.ARTIST)
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
                    val thisAlbumId = getLong(albumIdColumn)
                    val thisAlbumName = getString(albumColumn)
                    val thisDuration = getLong(durationColumn)
                    // tracks with <unknown> artist are often corrupted
                    if (thisArtist != "<unknown>") {
                        // set the song art
                        // build song object
                        val thisUri = uriFactory.getUri(thisId)
                        val artistNames = thisArtist.splitArtistNames()
                        val artists = mutableListOf<ArtistMetadata>()
                        artistNames.forEach { name ->
                            val key = name.lowercase()
                            if (!localArtistsMap.contains(key)) {
                                localArtistsMap[key] = MediaArtistData(
                                    id = localArtistId++,
                                    originalName = name
                                )
                            }
                            artists.add(
                                ArtistMetadata(
                                    id = localArtistsMap[key]!!.id,
                                    name = localArtistsMap[key]!!.originalName
                                )
                            )
                        }

                        val song = LocalSong(
                            uri = thisUri,
                            title = thisTitle,
                            artists = artists,
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
        return@withContext list.reversed()
    }
}
