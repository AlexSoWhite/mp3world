package com.nafanya.mp3world.features.localStorage.migrations

import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

@RenameTable(fromTableName = "FavouriteListEntity", toTableName = "FavouritesEntity")
class AutoMigration1314 : AutoMigrationSpec
