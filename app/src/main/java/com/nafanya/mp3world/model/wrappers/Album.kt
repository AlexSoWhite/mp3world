package com.nafanya.mp3world.model.wrappers

data class Album(
    val name: String = "",
    var songList: MutableList<Song>,
    val id: Long = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Album).id) return true
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + songList.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}
