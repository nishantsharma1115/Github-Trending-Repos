package com.nishant.githubtrendingrepos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrendingRepoEntity::class], version = 1, exportSchema = false)
abstract class TrendingRepoDatabase : RoomDatabase() {
    abstract fun trendingRepoDAO(): TrendingRepoDAO

    companion object {
        const val DATABASE_NAME: String = "task_db"
    }
}