package com.nafanya.mp3world.model.listManagers

import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.model.wrappers.Album
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Song
import java.io.IOException

@Suppress("LongMethod")
class MediaStoreReader {

    var context = PlayerApplication.context()

    companion object {
        private var isInitialized = false
        private const val artDimension = 1024
    }

    /**
     * Sets managers data on main thread.
     */
    fun readMediaStore() {
        if (!isInitialized) {
            initialize()
        }
    }

    /**
     * Resets SongListManager and other managers data on background thread.
     */
    fun reset() {
        initialize()
    }

    private fun initialize() {
        // get all the fields from media storage
        val projection = null
        // select only music
        var selection: String? = MediaStore.Audio.Media.IS_ALARM + "=0 AND " +
            MediaStore.Audio.Media.IS_NOTIFICATION + "=0 AND " +
            MediaStore.Audio.Media.IS_RINGTONE + "=0"
        val selectionArgs = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            selection =
                MediaStore.Audio.Media.IS_DOWNLOAD
        }
        // sort based on date
        val sortOrder = MediaStore.Audio.Media.DATE_MODIFIED
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "$sortOrder DESC"
        )?.use { cursor ->
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val artistIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
            val dateColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            while (cursor.moveToNext()) {
                // Use an ID column from the projection to get
                // a URI representing the media item itself.
                val thisId = cursor.getLong(idColumn)
                val thisTitle = cursor.getString(titleColumn)
                val thisArtist = cursor.getString(artistColumn)
                val thisDate = cursor.getLong(dateColumn)
                val thisArtistId = cursor.getLong(artistIdColumn)
                val thisAlbumId = cursor.getLong(albumIdColumn)
                val thisAlbumName = cursor.getString(albumColumn)
                val thisPath = cursor.getString(pathColumn)
                val thisDuration = cursor.getLong(durationColumn)
                // tracks with <unknown> artist are corrupted
                if (thisArtist != "<unknown>") {
                    // set the song art
                    val bitmap = getBitmap(context.contentResolver, thisId)
                    // build song object
                    val song = Song(
                        id = thisId,
                        title = thisTitle,
                        artist = thisArtist,
                        date = thisDate,
                        url = null,
                        duration = thisDuration,
                        path = thisPath,
                        art = bitmap
                    )
                    SongListManager.add(song)
                    val artist = Artist(
                        name = thisArtist,
                        id = thisArtistId
                    )
                    ArtistListManager.add(artist, song)
                    val album = Album(
                        id = thisAlbumId,
                        name = thisAlbumName,
                        songList = mutableListOf()
                    )
                    AlbumListManager.add(album, song)
                }
            }
        }
        dispatchThread()
        isInitialized = true
    }

    private fun dispatchThread() {
        if (!isInitialized) {
            SongListManager.resetDataOnMainThread()
            ArtistListManager.resetData()
            AlbumListManager.resetData()
        } else {
            SongListManager.resetData()
            ArtistListManager.resetData()
            AlbumListManager.resetData()
        }
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
        }
        return bitmap
    }
}
