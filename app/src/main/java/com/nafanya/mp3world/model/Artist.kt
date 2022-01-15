package com.nafanya.mp3world.model

data class Artist(
    val name: String? = null,
    val id: Long? = null,
    var playlist: Playlist? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this.id == (other as Artist).id) return true
        return false
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (playlist?.hashCode() ?: 0)
        return result
    }
}
