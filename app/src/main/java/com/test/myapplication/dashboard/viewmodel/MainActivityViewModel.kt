package com.test.myapplication.dashboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.myapplication.Repository.Repository

import com.test.myapplication.api.CustomResponse
import com.test.myapplication.api.getMediafromDevice
import com.test.myapplication.dashboard.model.MediaFileInfo

class MainActivityViewModel(application: Application, var repository: Repository) :
    AndroidViewModel(application) {

    private var responseLiveData = MutableLiveData<MutableList<MediaFileInfo>>()
    fun datafromFile() {

        getMediafromDevice(repository, getApplication(), customResponse = object : CustomResponse {
            override fun onSuccessResult(result: MutableList<MediaFileInfo>) {
                responseLiveData.postValue(result)


            }
        })
    }


    fun getAllData(): LiveData<MutableList<MediaFileInfo>> {
        return repository.getVideoDatalist
    }

    fun searchVideo(query: String?): LiveData<MutableList<MediaFileInfo>> {
        return repository.findSearchValue(query)
    }


}