package com.test.myapplication.Repository

import androidx.lifecycle.LiveData
import com.test.myapplication.dashboard.model.MediaFileInfo
import com.test.myapplication.db.database.AppDatabase

class Repository {
    private val appdatabase: AppDatabase

    constructor(appdatabase: AppDatabase) {
        this.appdatabase = appdatabase

    }

    val getVideoDatalist: LiveData<MutableList<MediaFileInfo>>
        get() = appdatabase.itemDataDao.all

    fun insertVideoData(data: MediaFileInfo) {
        appdatabase.itemDataDao.insert(data)
    }

    fun findSearchValue(query: String?): LiveData<MutableList<MediaFileInfo>> {
        return appdatabase.itemDataDao.findSearchValue(query)
    }


}