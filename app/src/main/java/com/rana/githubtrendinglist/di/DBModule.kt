package com.rana.githubtrendinglist.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.rana.data.db.AppDatabase
import com.rana.data.utils.SharedPrefsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * This is a Dagger provider module
 */
@InstallIn(SingletonComponent::class)
@Module
class DBModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SharedPrefsHelper.PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context, @Named("databaseName") dbName: String): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            dbName
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Named("databaseName")
    fun provideDatabaseName(): String = "TrendingRepo.db"

    @Singleton
    @Provides
    fun provideTrendingRepoDao(appDatabase: AppDatabase) = appDatabase.trendingRepoDao()
}