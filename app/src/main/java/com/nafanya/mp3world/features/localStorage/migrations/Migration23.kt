package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * migration from 2nd version of database to 3rd
 */
@Suppress("MagicNumber")
object Migration23 : Migration(2, 3) {
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
