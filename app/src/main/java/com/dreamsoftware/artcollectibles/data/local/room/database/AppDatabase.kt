package com.dreamsoftware.artcollectibles.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dreamsoftware.artcollectibles.data.local.room.converters.Converters
import com.dreamsoftware.artcollectibles.data.local.room.dao.metadata.TokenMetadataDAO
import com.dreamsoftware.artcollectibles.data.local.room.entity.TokenMetadataEntity

@Database(entities = [TokenMetadataEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tokenMetadataDAO(): TokenMetadataDAO

    companion object {
        const val DATABASE_NAME = "ArtCollectiblesDB"
    }
}
