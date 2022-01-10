package com.nafanya.mp3world.model

import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.ArrayList

object SongListManager {

    private val songList: ArrayList<Song> = ArrayList()
    private var isInitialized = false
    private const val multiplier = 1000L

    fun initializeSongList(activity: AppCompatActivity) {
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

            activity.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                "$sortOrder DESC"
            )?.use { cursor ->
                val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val dateColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
                while (cursor.moveToNext()) {
                    // Use an ID column from the projection to get
                    // a URI representing the media item itself.
                    val thisId = cursor.getLong(idColumn)
                    val thisTitle = cursor.getString(titleColumn)
                    val thisArtist = cursor.getString(artistColumn)
                    val thisDate = cursor.getInt(dateColumn)
                    songList.add(
                        Song(
                            thisId,
                            thisTitle,
                            thisArtist,
                            simpleDateFormat.format(thisDate * multiplier)
                        )
                    )
                }
            }
            isInitialized = true
        }
    }

    fun getSongList(): ArrayList<Song> {
        return songList
    }
}
