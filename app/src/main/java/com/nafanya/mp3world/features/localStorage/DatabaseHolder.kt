package com.nafanya.mp3world.features.localStorage

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.nafanya.mp3world.features.favorites.FavouriteListManager
import com.nafanya.mp3world.features.playlists.playlist.PlaylistStorageEntity
import com.nafanya.mp3world.features.playlists.playlist.toStorageEntity
import com.nafanya.mp3world.features.playlists.playlistsList.PlaylistListManager
import com.nafanya.mp3world.features.statistics.StatisticInfoManager
import javax.inject.Inject

@Suppress("MagicNumber")
/**
 * Class that provides access to local Room database. Populates playlists, favourites and statistics.
 * @property context holds application context.
 */
class DatabaseHolder @Inject constructor(
    private val context: Context
) {

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
            val playlists = mutableListOf<com.nafanya.player.Playlist>()
            val songListColumn = cursor.getColumnIndex("songList")
            val idColumn = cursor.getColumnIndex("id")
            val nameColumn = cursor.getColumnIndex("name")
            while (cursor.moveToNext()) {
                val string = cursor.getString(songListColumn)
                val songList = jsonToListOfSong(string)
                val thisId = cursor.getInt(idColumn)
                val thisName = cursor.getString(nameColumn)
                playlists.add(com.nafanya.player.Playlist(songList, thisId, thisName))
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

        private fun jsonToListOfSong(value: String): MutableList<com.nafanya.player.Song> {
            val list = Gson().fromJson(
                value,
                Array<com.nafanya.player.Song>::class.java
            ).toList()
            return if (list.isNotEmpty()) list as MutableList<com.nafanya.player.Song>
            else mutableListOf()
        }
    }

    private val migration45 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE FavouriteListEntity(
                    `id` INTEGER PRIMARY KEY NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    private val migration56 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                    CREATE TABLE SongStatisticEntity(
                            `id` INTEGER PRIMARY KEY NOT NULL,
                            `time` INTEGER,
                            `title` TEXT,
                            `artist` TEXT
                    )
                """.trimIndent()
            )
        }
    }

    private val migration67 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DELETE FROM SongStatisticEntity")
        }
    }

    private val migration78 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("DROP TABLE Song")
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
            migration34,
            migration45,
            migration56,
            migration67,
            migration78
        ).build()
    }

    fun populateLists() {
        StatisticInfoManager.initialize(db.songStatisticDao())
    }

    fun closeDataBase() {
        db.close()
    }
}
