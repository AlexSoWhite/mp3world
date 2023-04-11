package com.nafanya.mp3world.features.metadataEditing

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.nafanya.mp3world.core.wrappers.local.LocalSong
import javax.inject.Inject

class MetadataExtractor @Inject constructor(
    private val context: Context
) {

    private val ignoredFields = listOf(
        MediaStore.Audio.Media.ALBUM_KEY,
        MediaStore.Audio.Media.ARTIST_KEY,
        MediaStore.Audio.Media.TITLE_KEY,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ARTIST_ID,
//        MediaStore.Audio.Media._ID,
//        MediaStore.Audio.Media.TITLE,
//        MediaStore.Audio.Media.ARTIST,
//        MediaStore.Audio.Media.ALBUM,
//        "group_id",
//        "bucket_id"
    )

    @Synchronized
    fun getContentValues(song: LocalSong): ContentValues {
        val contentValues = ContentValues()
        context.contentResolver.query(
            song.uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                cursor.columnNames.filter {
                    !ignoredFields.contains(it)
                }.forEach {
                    cursor.put(contentValues, it)
                }
            }
        }
        return contentValues
    }

    private fun Cursor.put(contentValues: ContentValues, key: String) {
        val idx = getColumnIndex(key)
        when (getType(idx)) {
            Cursor.FIELD_TYPE_NULL -> {}
            Cursor.FIELD_TYPE_INTEGER -> contentValues.put(key, getLong(idx))
            Cursor.FIELD_TYPE_FLOAT -> contentValues.put(key, getDouble(idx))
            Cursor.FIELD_TYPE_STRING -> contentValues.put(key, getString(idx))
            Cursor.FIELD_TYPE_BLOB -> contentValues.put(key, getBlob(idx))
        }
    }
}
