package com.nafanya.mp3world.model.localStorage

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nafanya.mp3world.model.listManagers.PlaylistListManager
import com.nafanya.mp3world.model.listManagers.SongListManager

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

    init {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appDb"
        ).addMigrations(
            migration12,
            migration23
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
