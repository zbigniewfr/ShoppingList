package com.example.zbigniew.shoppinglist.dagger

import android.content.Context
import com.example.zbigniew.shoppinglist.repository.local.LocalDataProvider
import com.example.zbigniew.shoppinglist.repository.local.LocalDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {

    @Provides
    @Singleton
    fun provideLocalRepository(cxt: Context): LocalDataSource = LocalDataProvider().provide(cxt)

}

