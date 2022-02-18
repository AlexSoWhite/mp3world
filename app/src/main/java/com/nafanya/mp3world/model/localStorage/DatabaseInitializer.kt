package com.nafanya.mp3world.model.localStorage

import android.content.Context
import androidx.room.Room

object DatabaseInitializer {

    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appDb"
        ).build()
    }
}
