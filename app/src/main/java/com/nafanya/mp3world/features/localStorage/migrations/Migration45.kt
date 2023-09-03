package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
object Migration45 : Migration(4, 5) {
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
