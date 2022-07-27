package com.nafanya.mp3world

import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.nafanya.mp3world.features.localStorage.AppDatabase
import com.nafanya.mp3world.features.localStorage.DatabaseHolder
import com.nafanya.mp3world.features.playlists.playlist.PlaylistStorageEntity
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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

@Suppress("MaxLineLength")
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

        val song = com.nafanya.player.Song(
            id = 5,
            title = "testSong",
            artist = "testArtist",
            date = null,
            url = "sampleUrl"
        )
        // Migration from 1 to 2, Room 2.1.0
        val MIGRATION_1_2 = object : Migration(1, 2) {
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
                database.execSQL(
                    """
                    INSERT into Song (id, title, artist, date, url)
                    VALUES ("${song.id}", "${song.title}", "${song.artist}", "${song.date}", "${song.url}")
                    """.trimIndent()
                )
            }
        }

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }

    @Test
    fun migrate2To3() {
        val song = com.nafanya.player.Song(
            id = 5,
            title = "testSong",
            artist = "testArtist",
            date = null,
            url = "sampleUrl",
            duration = 100,
            path = "asdas"
        )
        val migration23 = object : Migration(2, 3) {
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
                database.execSQL(
                    """
                    INSERT into Song (id, title, artist, date, url, duration, path)
                    VALUES ("${song.id}", "${song.title}", "${song.artist}", "${song.date}", "${song.url}", "${song.duration}", "${song.path}")
                    """.trimIndent()
                )
            }
        }
        helper.createDatabase(TEST_DB, 2)
        helper.runMigrationsAndValidate(TEST_DB, 3, true, migration23)
    }

    @Suppress("LongMethod")
    @Test
    fun migrate3to4() {
        val migration34 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val song1 = com.nafanya.player.Song(
                    id = 1,
                    title = "testSong1",
                    artist = "testArtist",
                    date = null,
                    url = "sampleUrl",
                    duration = 100,
                    path = "asdas"
                )
                val song2 = com.nafanya.player.Song(
                    id = 2,
                    title = "testSong2",
                    artist = "testArtist",
                    date = null,
                    url = "sampleUrl",
                    duration = 100,
                    path = "asdas"
                )
                val song3 = com.nafanya.player.Song(
                    id = 3,
                    title = "testSong3",
                    artist = "testArtist",
                    date = null,
                    url = "sampleUrl",
                    duration = 100,
                    path = "asdas"
                )
                val song4 = com.nafanya.player.Song(
                    id = 5,
                    title = "testSong4",
                    artist = "testArtist",
                    date = null,
                    url = "sampleUrl",
                    duration = 100,
                    path = "asdas"
                )
                val song5 = com.nafanya.player.Song(
                    id = 6,
                    title = "testSong5",
                    artist = "testArtist",
                    date = null,
                    url = "sampleUrl",
                    duration = 100,
                    path = "asdas"
                )
                val song6 = com.nafanya.player.Song(
                    id = 7,
                    title = "testSong6",
                    artist = "testArtist",
                    date = null,
                    url = "sampleUrl",
                    duration = 100,
                    path = "asdas"
                )
                val playlist1 = com.nafanya.player.Playlist(
                    songList = mutableListOf(song1, song2, song3),
                    id = 1,
                    name = "playlist1"
                )
                val playlist2 = com.nafanya.player.Playlist(
                    songList = mutableListOf(song4, song5, song6),
                    id = 2,
                    name = "playlist2"
                )
                database.execSQL(
                    """
                        INSERT into playlist (songList, id, name)
                        VALUES ("${playlist1.songList}", "${playlist1.id}", "${playlist1.name}")
                    """.trimIndent()
                )
                database.execSQL(
                    """
                        INSERT into playlist (songList, id, name)
                        VALUES ("${playlist2.songList}", "${playlist2.id}", "${playlist2.name}")
                    """.trimIndent()
                )
                val cursor = database.query("SELECT * FROM playlist")
                val playlists = mutableListOf<com.nafanya.player.Playlist>()
                val songListColumn = cursor.getColumnIndex("songList")
                val idColumn = cursor.getColumnIndex("id")
                val nameColumn = cursor.getColumnIndex("name")
                while (cursor.moveToNext()) {
                    var string = cursor.getString(songListColumn)
                    string = string.slice(1 until string.length - 1)
                    val rawSongs = string.split("),")
                    val songList = mutableListOf<com.nafanya.player.Song>()
                    rawSongs.forEach {
                        songList.add(
                            jsonToSong(
                                "{${
                                it.slice(5 until it.length)
                                    .replace("(", "")
                                    .replace(")", "")
                                }}"
                            )
                        )
                    }
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

            private fun jsonToSong(value: String): com.nafanya.player.Song {
                return Gson().fromJson(
                    value,
                    com.nafanya.player.Song::class.java
                )
            }
        }
        helper.createDatabase(TEST_DB, 3)
        helper.runMigrationsAndValidate(TEST_DB, 4, true, migration34)
    }

    @Test
    fun migrate4to5() {
        val migration45 = object : Migration(4, 5) {
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
        helper.createDatabase(TEST_DB, 4)
        helper.runMigrationsAndValidate(TEST_DB, 5, true, migration45)
    }

    @Test
    fun migrate5to6() {
        val migration56 = object : Migration(5, 6) {
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
        helper.createDatabase(TEST_DB, 5)
        helper.runMigrationsAndValidate(TEST_DB, 6, true, migration56)
    }
}

@RunWith(AndroidJUnit4::class)
class SongListStorageTest {

    private var db: AppDatabase = DatabaseHolder(
        InstrumentationRegistry.getInstrumentation().targetContext
    ).db
    private var songListDao: SongDao = db.songsListDao()

    fun testAdd(song: com.nafanya.player.Song) {
        songListDao.insert(song)
    }
}
