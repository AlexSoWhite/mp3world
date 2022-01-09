package com.nafanya.mp3world.model

import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity

object SongListManager {

    private val songList: ArrayList<Song> = ArrayList()

    private var isInitialized = false

    fun initializeSongList(activity: AppCompatActivity) {
        if (!isInitialized) {
            val projection = null
            val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
            val selectionArgs = null
            val sortOrder = null

            activity.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                while (cursor.moveToNext()) {
                    // Use an ID column from the projection to get
                    // a URI representing the media item itself.
                    val thisId = cursor.getLong(idColumn)
                    val thisTitle = cursor.getString(titleColumn)
                    val thisArtist = cursor.getString(artistColumn)
                    // val uri = MediaStore.Audio.Media.getContentUri()
                    songList.add(Song(thisId, thisTitle, thisArtist))
                }
            }
            isInitialized = true
        }
    }

    fun getSongList(): ArrayList<Song> {
        return songList
    }
}
