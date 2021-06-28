package com.test.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.myapplication.dashboard.model.MediaFileInfo

@Dao
interface ItemDataDao {

    @get:Query("SELECT * FROM Video_db")
    val all: LiveData<MutableList<MediaFileInfo>>

    @Query("SELECT * FROM Video_db WHERE fileName LIKE '%' || :query || '%'")
    fun findSearchValue(query: String?): LiveData<MutableList<MediaFileInfo>>

    @Insert
    fun insert(data: MediaFileInfo)

    @Delete
    fun delete(data: MediaFileInfo)

    @Update
    fun update(data: MediaFileInfo)

}
