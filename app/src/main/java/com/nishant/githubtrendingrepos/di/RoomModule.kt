package com.nishant.githubtrendingrepos.di

import android.content.Context
import androidx.room.Room
import com.nishant.githubtrendingrepos.data.local.CacheMapper
import com.nishant.githubtrendingrepos.data.local.TrendingRepoDAO
import com.nishant.githubtrendingrepos.data.local.TrendingRepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun provideTaskDb(@ApplicationContext context: Context): TrendingRepoDatabase {
        return Room.databaseBuilder(
            context,
            TrendingRepoDatabase::class.java,
            TrendingRepoDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTaskDAO(trendingRepoDatabase: TrendingRepoDatabase): TrendingRepoDAO {
        return trendingRepoDatabase.trendingRepoDAO()
    }

    @Singleton
    @Provides
    fun provideCacheMapper(): CacheMapper {
        return CacheMapper()
    }
}