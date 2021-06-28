package com.test.myapplication

import android.app.Application
import com.test.myapplication.di.AppComponent
import com.test.myapplication.di.DaggerAppComponent
import com.test.myapplication.di.UtilsModule


class MyApplication : Application() {
    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().utilsModule(UtilsModule(this)).build()
    }
}