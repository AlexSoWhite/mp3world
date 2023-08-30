package com.nafanya.mp3world.features.songListViews

import androidx.annotation.IntDef
import androidx.recyclerview.widget.DiffUtil
import com.nafanya.mp3world.core.listUtils.recycler.BaseListItem
import com.nafanya.mp3world.core.wrappers.SongWrapper

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    SONG_LOCAL_IMMUTABLE,
    SONG_REMOTE,
    SONG_REARRANGEABLE,
    SONG_ADDABLE_REMOVABLE,
    DATE,
    LOADER,
    MODIFY_PLAYLIST_BUTTON,
    CONFIRM_CHANGES_BUTTON
)
annotation class ListItemType
const val SONG_LOCAL_IMMUTABLE = 0
const val SONG_REMOTE = 1
const val SONG_REARRANGEABLE = 2
const val SONG_ADDABLE_REMOVABLE = 3
const val DATE = 4
const val LOADER = 5
const val MODIFY_PLAYLIST_BUTTON = 6
const val CONFIRM_CHANGES_BUTTON = 7

data class SongListItem(
    @ListItemType
    override val itemType: Int,
    override val data: Any
) : BaseListItem() {

    fun getDataAsSong(): SongWrapper {
        return data as SongWrapper
    }

    fun getDataAsDate(): String {
        return data as String
    }
}

val SongListItemDiffUtilCallback = object : DiffUtil.ItemCallback<SongListItem>() {

    override fun areItemsTheSame(oldItem: SongListItem, newItem: SongListItem): Boolean {
        if (oldItem.itemType != newItem.itemType) return false
        return when (oldItem.itemType) {
            SONG_REMOTE,
            SONG_ADDABLE_REMOVABLE,
            SONG_LOCAL_IMMUTABLE,
            SONG_REARRANGEABLE -> oldItem.getDataAsSong() == newItem.getDataAsSong()
            DATE -> oldItem.getDataAsDate() == newItem.getDataAsDate()
            else -> true
        }
    }

    override fun areContentsTheSame(oldItem: SongListItem, newItem: SongListItem): Boolean {
        // we consider that equal items contains equal contents
        if (oldItem.itemType != newItem.itemType) return false
        return when (oldItem.itemType) {
            SONG_REMOTE,
            SONG_ADDABLE_REMOVABLE,
            SONG_LOCAL_IMMUTABLE,
            SONG_REARRANGEABLE -> {
                val song1 = oldItem.getDataAsSong()
                val song2 = newItem.getDataAsSong()
                (song1.uri == song2.uri) &&
                    (song1.title == song2.title) &&
                    (song1.artist == song2.artist) &&
                    (song1.art == song2.art)
            }
            DATE -> oldItem.getDataAsDate() == newItem.getDataAsDate()
            else -> true
        }
    }
}
