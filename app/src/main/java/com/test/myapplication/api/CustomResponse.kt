package com.test.myapplication.api

import com.test.myapplication.dashboard.model.MediaFileInfo


interface CustomResponse {
    fun onSuccessResult(result: MutableList<MediaFileInfo>) {}

}