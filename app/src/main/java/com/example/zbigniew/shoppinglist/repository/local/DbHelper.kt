package com.example.zbigniew.shoppinglist.repository.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper constructor(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "shoppinglist_db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbSchema.ShoppingList.CREATE_TABLE)
        db.execSQL(DbSchema.Item.CREATE_TABLE)
        db.execSQL(DbSchema.Index.CREATE_INDEX)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DbSchema.ShoppingList.TABLE}")
        db.execSQL("DROP TABLE IF EXISTS ${DbSchema.Item.TABLE}")
        onCreate(db)
    }

}