package com.nafanya.mp3world.model.localStorage

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager
import com.nafanya.mp3world.model.wrappers.Playlist
import com.nafanya.mp3world.model.wrappers.PlaylistStorageEntity
import com.nafanya.mp3world.model.wrappers.Song

@Suppress("MagicNumber")
class DatabaseHolder(context: Context) {

    var db: AppDatabase

    /**
     * migration from 1st version of database to 2nd
     */
    private val migration12 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                    CREATE TABLE Song (
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `title` TEXT,
                        `artist` TEXT,
                        `date` TEXT,
                        `url` TEXT
                    )
                """.trimIndent()
            )
        }
    }

    /**
     * migration from 2nd version of database to 3rd
     */
    private val migration23 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                    ALTER TABLE Song ADD COLUMN duration INTEGER 
                """.trimIndent()
            )
            database.execSQL(
                """
                    ALTER TABLE Song ADD COLUMN path TEXT 
                """.trimIndent()
            )
        }
    }

    /**
     * migration from 3rd version of database to 4th
     */
    private val migration34 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val cursor = database.query("SELECT * FROM playlist")
            val playlists = mutableListOf<Playlist>()
            val songListColumn = cursor.getColumnIndex("songList")
            val idColumn = cursor.getColumnIndex("id")
            val nameColumn = cursor.getColumnIndex("name")
            while (cursor.moveToNext()) {
                val string = cursor.getString(songListColumn)
                val songList = jsonToListOfSong(string)
                val thisId = cursor.getInt(idColumn)
                val thisName = cursor.getString(nameColumn)
                playlists.add(
                    Playlist(
                        songList, thisId, thisName
                    )
                )
            }
            val newPlaylists = mutableListOf<PlaylistStorageEntity>()
            playlists.forEach {
                newPlaylists.add(it.toStorageEntity())
            }
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
                            VALUES ("${it.id}", "${it.songIds}", "${it.name}")
                        """.trimIndent()
                )
            }
            database.execSQL("DROP TABLE Playlist")
        }

        private fun jsonToSong(value: String): Song {
            return Gson().fromJson(
                value,
                Song::class.java
            )
        }

        private fun jsonToListOfSong(value: String): MutableList<Song> {
            val list = Gson().fromJson(
                value,
                Array<Song>::class.java
            ).toList()
            return if (list.isNotEmpty()) list as MutableList<Song>
            else mutableListOf()
        }
    }

    init {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appDb"
        ).addMigrations(
            migration12,
            migration23,
            migration34
        ).build()
    }

    fun populateLists() {
        PlaylistListManager.initialize(db.playlistDao())
        SongListManager.appendLocalSongs(db.songsListDao())
    }

    fun closeDataBase() {
        db.close()
    }
}
