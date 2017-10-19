package com.example.zbigniew.shoppinglist.repository.local

import android.content.ContentValues
import com.example.zbigniew.shoppinglist.model.ItemModel
import com.example.zbigniew.shoppinglist.model.ShoppingListModel


abstract class ValuesProvider<out T> constructor(private val obj: T) {
    val cv: ContentValues = ContentValues()
    abstract fun convert()
    fun toContentValues(): ContentValues {
        convert()
        return cv
    }
}


class ShoppingListValueProvider(private val shoppingList: ShoppingListModel) : ValuesProvider<ShoppingListModel>(shoppingList) {
    override fun convert() {
        cv.put(DbSchema.ShoppingList.NAME, shoppingList.name)
        cv.put(DbSchema.ShoppingList.ARCHIVED, if (shoppingList.isArchived) 1 else 0)
        cv.put(DbSchema.ShoppingList.CHECKED_ITEMS, shoppingList.checkedItemsCount)
        cv.put(DbSchema.ShoppingList.ALL_ITEMS, shoppingList.allItemsCount)
        cv.put(DbSchema.ShoppingList.CREATE_DATE, shoppingList.createDate)
    }
}

class ItemValueProvider(private val item: ItemModel) : ValuesProvider<ItemModel>(item) {
    override fun convert() {
        cv.put(DbSchema.Item.NAME, item.name)
        cv.put(DbSchema.Item.IS_CHECKED, item.isChecked)
        cv.put(DbSchema.Item.LIST_ID, item.listId)
    }
}