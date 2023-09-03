package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.nafanya.mp3world.core.wrappers.PlaylistWrapper
import com.nafanya.mp3world.core.wrappers.SongWrapper
import com.nafanya.mp3world.features.allPlaylists.model.PlaylistStorageEntity

/**
 * migration from 3rd version of database to 4th
 */
@Suppress("MagicNumber")
object Migration34 : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val cursor = database.query("SELECT * FROM playlist")
        val playlists = mutableListOf<PlaylistWrapper>()
        val songListColumn = cursor.getColumnIndex("songList")
        val idColumn = cursor.getColumnIndex("id")
        val nameColumn = cursor.getColumnIndex("name")
        while (cursor.moveToNext()) {
            val string = cursor.getString(songListColumn)
            val songList = jsonToListOfSong(string)
            val thisId = cursor.getLong(idColumn)
            val thisName = cursor.getString(nameColumn)
            playlists.add(PlaylistWrapper(songList, thisId, thisName))
        }
        val newPlaylists = mutableListOf<PlaylistStorageEntity>()
        database.execSQL(
            """
                    CREATE TABLE PlaylistStorageEntity (
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `songIds` TEXT,
                        `name` TEXT NOT NULL
                    )
            """.trimIndent()
        )
        newPlaylists.forEach {
            database.execSQL(
                """
                        INSERT into PlaylistStorageEntity (id, songIds, name)
                        VALUES ("${it.id}", "-1", "${it.name}")
                """.trimIndent()
            )
        }
        database.execSQL("DROP TABLE Playlist")
    }

    private fun jsonToListOfSong(value: String): MutableList<SongWrapper> {
        val list = Gson().fromJson(
            value,
            Array<SongWrapper>::class.java
        ).toList()
        return if (list.isNotEmpty()) list as MutableList<SongWrapper>
        else mutableListOf()
    }
}
