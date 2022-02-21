package com.nafanya.mp3world.model.localStorage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.nafanya.mp3world.model.wrappers.Song

class Converter {
    @TypeConverter
    fun jsonToList(value: String): MutableList<Song> {
        val list = Gson().fromJson(
            value,
            Array<Song>::class.java
        ).toList()
        return if (list.isNotEmpty()) list as MutableList<Song>
        else mutableListOf()
    }

    @TypeConverter
    fun listToJson(value: MutableList<Song>) = Gson().toJson(value)
}