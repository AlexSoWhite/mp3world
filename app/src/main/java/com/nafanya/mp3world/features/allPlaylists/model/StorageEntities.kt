package com.nafanya.mp3world.features.allPlaylists.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(indices = [Index(value = ["id"])])
data class PlaylistStorageEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "position") val position: Int
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PlaylistStorageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("playlistId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaylistSongsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "playlistId") val playlistId: Long,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "position") val position: Int
)

data class PlaylistWithSongs(
    @Embedded val playlistEntity: PlaylistStorageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
    )
    val songEntities: List<PlaylistSongsEntity>
)
