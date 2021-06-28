package com.test.myapplication.api

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import com.test.myapplication.Repository.Repository
import com.test.myapplication.dashboard.model.MediaFileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CheckResult")
fun ViewModel.getMediafromDevice(
    repository: Repository,
    context: Context,
    customResponse: CustomResponse
) {
    val c: Context = context
    var mediaList: MutableList<MediaFileInfo> = ArrayList()
    GlobalScope.launch(Dispatchers.IO) {
        var result = async(Dispatchers.IO) {
            try {
                var name: String? = null
                val thumbColumns: Array<String> = arrayOf(
                    MediaStore.Video.Thumbnails.DATA,
                    MediaStore.Video.Thumbnails.VIDEO_ID
                )
                var video_column_index: Int = 0
                val proj: Array<String> = arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.SIZE
                )
                val videocursor: Cursor? = context.applicationContext.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    proj, null, null, null
                )
                var count: Int? = videocursor?.getCount()
                Log.d("NoOfVideo", "" + count)
                var i = 0
                while (i < 10!!) {

                    if (videocursor != null) {
                        video_column_index = videocursor
                            ?.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    }
                    videocursor?.moveToPosition(i)
                    name = videocursor?.getString(video_column_index)

                    val column_index: Int? =
                        videocursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    videocursor?.moveToPosition(i)
                    val filepath: String? = column_index?.let { it1 -> videocursor?.getString(it1) }
                    val mediaFileInfo: MediaFileInfo =
                        MediaFileInfo(i.toLong(), name, filepath, "video")
                    mediaList.add(mediaFileInfo)
                    repository.insertVideoData(mediaFileInfo)
                    i++
                }
                videocursor?.close()


            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@async mediaList
        }
        parseResponse(result.await(), customResponse)
    }


}

fun parseResponse(await: MutableList<MediaFileInfo>, customResponse: CustomResponse) {
    Log.i("heeee", await.toString())
    customResponse.onSuccessResult(await)
}


