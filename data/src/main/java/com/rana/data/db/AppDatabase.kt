package com.rana.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rana.domain.entity.RepositoryItemEntity

@Database(entities = [RepositoryItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trendingRepoDao(): TrendingRepoDao
}