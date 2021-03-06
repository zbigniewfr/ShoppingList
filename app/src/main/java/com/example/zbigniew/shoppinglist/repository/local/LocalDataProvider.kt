package com.example.zbigniew.shoppinglist.repository.local

import android.content.Context
import com.squareup.sqlbrite2.SqlBrite
import io.reactivex.schedulers.Schedulers

class LocalDataProvider {
    fun provide(cxt: Context): LocalDataSource {
        return LocalDataSource(SqlBrite.Builder().build().wrapDatabaseHelper(DbHelper(cxt), Schedulers.io()))
    }

}