package com.test.myapplication.dashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Mukesh on 12/20/2015.
 */
@Entity(tableName = "Video_db")
data class MediaFileInfo(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("fileName") val fileName: String?,
    @field:SerializedName("filePath") val filePath: String?,
    @field:SerializedName("fileType") val fileType: String?
) : Serializable