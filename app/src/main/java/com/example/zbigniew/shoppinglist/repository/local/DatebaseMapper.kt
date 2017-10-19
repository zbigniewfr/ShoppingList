package com.example.zbigniew.shoppinglist.repository.local

import android.database.Cursor
import com.example.zbigniew.shoppinglist.model.ItemModel
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import io.reactivex.functions.Function

class DatabaseMapper {
    val BOOLEAN_FALSE = 0
    val BOOLEAN_TRUE = 1

    fun getString(cursor: Cursor, columnName: String): String {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName))
    }

    fun getBoolean(cursor: Cursor, columnName: String): Boolean {
        return getInt(cursor, columnName) == BOOLEAN_TRUE
    }

    fun getLong(cursor: Cursor, columnName: String): Long {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName))
    }

    fun getInt(cursor: Cursor, columnName: String): Int {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName))
    }

    fun getDouble(cursor: Cursor, columnName: String): Double {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName))
    }

    private fun Db() {
        throw AssertionError("No instances.")
    }

    inner class ShoppingListsDatabaseMapper {
        var MAPPER: Function<Cursor, ShoppingListModel> = Function<Cursor, ShoppingListModel> { cursor ->
            val id = getLong(cursor, DbSchema.ShoppingList.ID)
            val name = getString(cursor, DbSchema.ShoppingList.NAME)
            val archived = getBoolean(cursor, DbSchema.ShoppingList.ARCHIVED)
            val checkedItems = getInt(cursor, DbSchema.ShoppingList.CHECKED_ITEMS)
            val allItems = getInt(cursor, DbSchema.ShoppingList.ALL_ITEMS)
            val createDate = getLong(cursor, DbSchema.ShoppingList.CREATE_DATE)
            ShoppingListModel(id, name, createDate, archived, mutableListOf<ItemModel>(), checkedItems, allItems)
        }
    }

    inner class ItemDatabaseMapper {
        var MAPPER: Function<Cursor, ItemModel> = Function<Cursor, ItemModel> { cursor ->
            val id = getLong(cursor, DbSchema.Item.ID)
            val name = getString(cursor, DbSchema.Item.NAME)
            val isChecked = getBoolean(cursor, DbSchema.Item.IS_CHECKED)
            val listId = getLong(cursor, DbSchema.Item.LIST_ID)
            ItemModel(id, name, isChecked, listId)
        }
    }
}