package com.nafanya.mp3world.model.listManagers

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.nafanya.mp3world.model.wrappers.Artist
import com.nafanya.mp3world.model.wrappers.Song
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.ArrayList

object SongListManager {

    private val songList: ArrayList<Song> = ArrayList()

    private var isInitialized = false
    private const val multiplier = 1000L

    fun initializeSongList(context: Context) {
        if (!isInitialized) {
            val projection = null
            var selection: String? = MediaStore.Audio.Media.IS_MUSIC + "!=0"
            val selectionArgs = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                selection =
                    MediaStore.Audio.Media.IS_MUSIC +
                    "!=0 AND " +
                    MediaStore.Audio.Media.IS_RECORDING +
                    "=0"
            }
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
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
                while (cursor.moveToNext()) {
                    // Use an ID column from the projection to get
                    // a URI representing the media item itself.
                    val thisId = cursor.getLong(idColumn)
                    val thisTitle = cursor.getString(titleColumn)
                    val thisArtist = cursor.getString(artistColumn)
                    val thisDate = cursor.getInt(dateColumn)
                    val thisArtistId = cursor.getLong(artistIdColumn)
                    val song = Song(
                        id = thisId,
                        title = thisTitle,
                        artist = thisArtist,
                        date = simpleDateFormat.format(thisDate * multiplier),
                        url = null
                    )
                    songList.add(song)
                    val artist = Artist(
                        thisArtist,
                        thisArtistId
                    )
                    ArtistListManager.add(artist, song)
                }
            }
            isInitialized = true
        }
    }

    fun getSongList(): ArrayList<Song> {
        return songList
    }
}
