package com.nishant.githubtrendingrepos.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendingRepoDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrendingRepo(repo: TrendingRepoEntity): Long

    @Query("select * from TrendingRepo")
    fun getAllTrendingRepos(): Flow<List<TrendingRepoEntity>>

    @Query("select * from TrendingRepo where fullName like '%' || :query || '%' ")
    fun getSearchedRepos(query: String): Flow<List<TrendingRepoEntity>>
}