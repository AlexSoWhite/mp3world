package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * migration from 1st version of database to 2nd
 */
@Suppress("MagicNumber")
object Migration12 : Migration(1, 2) {
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
