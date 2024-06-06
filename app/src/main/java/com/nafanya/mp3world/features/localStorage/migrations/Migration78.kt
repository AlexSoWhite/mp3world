package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
object Migration78 : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE Song")
    }
}
