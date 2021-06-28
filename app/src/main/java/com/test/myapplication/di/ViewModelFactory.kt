package com.test.myapplication.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.myapplication.Repository.Repository
import com.test.myapplication.dashboard.viewmodel.MainActivityViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(var context: Application, var repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(context, repository) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}