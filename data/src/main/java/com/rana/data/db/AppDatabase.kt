package com.rana.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rana.data.models.RepositoryItemDto
import com.rana.domain.entity.RepositoryItemEntity

@Database(entities = [RepositoryItemDto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trendingRepoDao(): TrendingRepoDao
}