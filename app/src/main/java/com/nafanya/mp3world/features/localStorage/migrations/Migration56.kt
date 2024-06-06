package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
object Migration56 : Migration(5, 6) {
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
