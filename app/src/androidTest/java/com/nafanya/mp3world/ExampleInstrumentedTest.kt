package com.nafanya.mp3world

import androidx.room.migration.Migration
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.nafanya.mp3world.model.localStorage.AppDatabase
import com.nafanya.mp3world.model.localStorage.DatabaseHolder
import com.nafanya.mp3world.model.localStorage.SongListDao
import com.nafanya.mp3world.model.wrappers.Song
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.nafanya.mp3world", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB, 1).apply {
            execSQL("SELECT * FROM playlist")
            // Prepare for the next version.
            close()
        }

        val song = Song(
            id = 5,
            title = "testSong",
            artist = "testArtist",
            date = null,
            url = "sampleUrl"
        )
        // Migration from 1 to 2, Room 2.1.0
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE Song (
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `title` TEXT,
                        `artist` TEXT,
                        `date` TEXT,
                        `url` TEXT
                    )
                """.trimIndent())
                database.execSQL("""
                    INSERT into Song (id, title, artist, date, url)
                    VALUES ("${song.id}", "${song.title}", "${song.artist}", "${song.date}", "${song.url}")
                """.trimIndent())
            }
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }
}

@RunWith(AndroidJUnit4::class)
class SongListStorageTest {

    private var db: AppDatabase
    private var songListDao: SongListDao
    init {
        DatabaseHolder.init(InstrumentationRegistry.getInstrumentation().targetContext)
        db = DatabaseHolder.db
        songListDao = db.songsListDao()
    }

    fun testAdd(song: Song) {
        songListDao.insert(song)
    }

}