package com.rana.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rana.data.models.RepositoryItemDto
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the UserProductCart entity
 */
@Dao
interface TrendingRepoDao {
    /**
     * Select all cart from the TrendingRepoEntity.
     *
     * @return all repos.
     */
    @Query("SELECT * FROM RepositoryItemDto")
     fun allTrendingRepos(): Flow<List<RepositoryItemDto>>

    /**
     * Insert all repose
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun saveTrendingRepos(userProductCart: List<RepositoryItemDto>)

    /**
     * Delete all repos
     */
    @Query("DELETE FROM RepositoryItemDto")
     fun deleteAllTrendingRepos()

    /**
     * Delete all repos
     */
    @Query("SELECT COUNT(*) FROM RepositoryItemDto")
     fun isReposCacheAvailable(): Int
}