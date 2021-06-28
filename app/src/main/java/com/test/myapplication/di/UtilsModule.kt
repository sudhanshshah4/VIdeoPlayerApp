package com.test.myapplication.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.test.myapplication.Repository.Repository
import com.test.myapplication.db.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule(var context: Application) {

    @Provides
    @Singleton
    fun getAppContext(): Application {
        return context
    }

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }

    @Provides
    fun getRepository(appDatabase: AppDatabase): Repository {
        return Repository(appDatabase)
    }

    @Provides
    @Singleton
    fun getAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "MyTEST")
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }
}