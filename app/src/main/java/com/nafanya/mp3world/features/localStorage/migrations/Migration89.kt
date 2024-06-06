package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
object Migration89 : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE SongStatisticEntity")
    }
}
