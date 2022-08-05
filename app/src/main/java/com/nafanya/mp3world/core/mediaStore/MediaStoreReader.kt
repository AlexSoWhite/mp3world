package com.nafanya.mp3world.core.mediaStore

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.nafanya.mp3world.core.listManagers.ListManagerContainer
import com.nafanya.player.Song
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("LongMethod")
/**
 * Class that reads local MediaStore.
 * @property allSongs holds all song objects that device has
 */
class MediaStoreReader @Inject constructor(
    private val context: Context,
    private val listManagerContainer: ListManagerContainer
) {

    private var mAllSongs: List<Song> = listOf()
    val allSongs
        get() = mAllSongs

    companion object {
        private var isInitialized = false
        private const val artDimension = 1024
    }

    /**
     * Sets managers data on main thread.
     */
    suspend fun readMediaStore() {
        if (!isInitialized) {
            initialize()
        }
    }

    /**
     * Resets SongListManager and other managers data on background thread.
     */
    suspend fun reset() {
        initialize()
    }

    private suspend fun initialize() = withContext(Dispatchers.IO) {
        // get all the fields from media storage
        val projection = null
        // select only music
        var selection: String? = MediaStore.Audio.Media.IS_MUSIC + "=1"
        val selectionArgs = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            selection =
                MediaStore.Audio.Media.IS_DOWNLOAD
        }
        // sort based on date
        val sortOrder = MediaStore.Audio.Media.DATE_MODIFIED
        val allSongsList = mutableListOf<Song>()
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
                    // tracks with <unknown> artist are corrupted
                    if (thisArtist != "<unknown>") {
                        // set the song art
                        val bitmap = getBitmap(context.contentResolver, thisId)
                        // build song object
                        val song = Song(
                            id = thisId,
                            title = thisTitle,
                            artistId = thisArtistId,
                            artist = thisArtist,
                            albumId = thisAlbumId,
                            album = thisAlbumName,
                            date = thisDate,
                            url = null,
                            duration = thisDuration,
                            art = bitmap,
                            artUrl = null
                        )
                        allSongsList.add(song)
                    }
                }
            }
        }
        mAllSongs = allSongsList
        listManagerContainer.populateAll(this@MediaStoreReader)
        isInitialized = true
    }

    private fun getBitmap(contentResolver: ContentResolver, trackId: Long): Bitmap? {
        val trackUri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            trackId
        )
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            bitmap = try {
                contentResolver.loadThumbnail(trackUri, Size(artDimension, artDimension), null)
            } catch (exception: IOException) {
                null
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(trackUri.path)
            var inputStream: InputStream? = null
            if (retriever.embeddedPicture != null) {
                inputStream = ByteArrayInputStream(retriever.embeddedPicture)
            }
            retriever.release()
            bitmap = BitmapFactory.decodeStream(inputStream)
        }
        return bitmap
    }
}
