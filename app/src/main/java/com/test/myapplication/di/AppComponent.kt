package com.test.myapplication.di

import com.test.myapplication.dashboard.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [UtilsModule::class])
@Singleton
interface AppComponent {
    fun doMainInjection(main_activity: MainActivity?)
}