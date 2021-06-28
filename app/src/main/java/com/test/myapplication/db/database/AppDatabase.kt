package com.test.myapplication.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.myapplication.dashboard.model.MediaFileInfo
import com.test.myapplication.db.dao.ItemDataDao

@Database(entities = [MediaFileInfo::class], version = 10)
abstract class AppDatabase : RoomDatabase() {

    abstract val itemDataDao: ItemDataDao


}