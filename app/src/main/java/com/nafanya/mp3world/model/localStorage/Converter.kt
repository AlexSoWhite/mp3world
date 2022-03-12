package com.nafanya.mp3world.model.localStorage

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun jsonToList(value: String): MutableList<Long> {
        val list = Gson().fromJson(
            value,
            Array<Long>::class.java
        ).toList()
        return if (list.isNotEmpty()) list as MutableList<Long>
        else mutableListOf()
    }

    @TypeConverter
    fun listToJson(value: MutableList<Long>): String {
        return Gson().toJson(value)
    }
}
