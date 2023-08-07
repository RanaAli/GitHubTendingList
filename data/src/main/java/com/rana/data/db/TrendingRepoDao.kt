package com.rana.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rana.domain.entity.RepositoryItemEntity

/**
 * Data Access Object for the UserProductCart entity
 */
@Dao
interface TrendingRepoDao {
    /**
     * Select all cart from the TrendingRepoEntity.
     *
     * @return all products in cart.
     */
    @Query("SELECT * FROM RepositoryItemDao")
    suspend fun allTrendingRepos(): List<RepositoryItemEntity>

    /**
     * Insert all repose
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrendingRepos(userProductCart: List<RepositoryItemEntity?>?)

    /**
     * Delete all repos
     */
    @Query("DELETE FROM RepositoryItemDao")
    suspend fun deleteAllTrendingRepos()

    /**
     * Delete all repos
     */
    @Query("SELECT COUNT(*) FROM RepositoryItemDao")
    suspend fun isReposCacheAvailable(): Int
}