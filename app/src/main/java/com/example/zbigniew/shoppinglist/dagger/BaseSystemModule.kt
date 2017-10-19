package com.example.zbigniew.shoppinglist.dagger

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class BaseSystemModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }
}
