package com.example.zbigniew.shoppinglist.repository.local

import android.database.sqlite.SQLiteDatabase
import com.example.zbigniew.shoppinglist.model.ItemModel
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import com.squareup.sqlbrite2.BriteDatabase
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalDataSource(private val db: BriteDatabase) {

    fun saveShoppingList(shoppingList: ShoppingListModel): Long {
        db.newTransaction().use { transactions ->
            val id = db.insert(DbSchema.ShoppingList.TABLE, ShoppingListValueProvider(shoppingList).toContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
            transactions.markSuccessful()
            return id
        }
    }

    fun getListById(listId: Long): Flowable<ShoppingListModel>  =
            db.createQuery(DbSchema.ShoppingList.TABLE, getShoppingListById(listId))
                .mapToOne(DatabaseMapper().ShoppingListsDatabaseMapper().MAPPER)
                .toFlowable(BackpressureStrategy.BUFFER)

    fun getAllLists(): Flowable<MutableList<ShoppingListModel>> =
            db.createQuery(DbSchema.ShoppingList.TABLE, selectAllShoppingLists())
                .mapToList(DatabaseMapper().ShoppingListsDatabaseMapper().MAPPER)
                .toFlowable(BackpressureStrategy.BUFFER)

    fun getAllArchivedLists(): Flowable<MutableList<ShoppingListModel>> =
            db.createQuery(DbSchema.ShoppingList.TABLE, selectAllArchivedShoppingLists())
                    .mapToList(DatabaseMapper().ShoppingListsDatabaseMapper().MAPPER)
                    .toFlowable(BackpressureStrategy.BUFFER)

    fun getItems(listId: Long): Flowable<MutableList<ItemModel>> =
            db.createQuery(DbSchema.ShoppingList.TABLE, selectItemsFromList(listId))
                    .mapToList(DatabaseMapper().ItemDatabaseMapper().MAPPER)
                    .toFlowable(BackpressureStrategy.BUFFER)

    fun removeItem(itemId: Long) {
        db.newTransaction().use { transactions ->
            db.delete(DbSchema.Item.TABLE, "${DbSchema.Item.ID} = $itemId")
            transactions.markSuccessful()
        }
    }

    fun addItem(item: ItemModel): Long {
        db.newTransaction().use { transactions ->
            val id = db.insert(DbSchema.Item.TABLE, ItemValueProvider(item).toContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
            transactions.markSuccessful()
            return id
        }
    }

    fun getItem(itemId: Long): Flowable<ItemModel>  =
            db.createQuery(DbSchema.Item.TABLE, getItemById(itemId))
                .mapToOne(DatabaseMapper().ItemDatabaseMapper().MAPPER)
                .toFlowable(BackpressureStrategy.BUFFER)

    fun updateItem(item: ItemModel) {
        db.newTransaction().use { transactions ->
            db.update(DbSchema.Item.TABLE, ItemValueProvider(item).toContentValues(), "${DbSchema.Item.ID}=?", item.id.toString())
            transactions.markSuccessful()
        }
    }

    fun updateShoppingList(list: ShoppingListModel) {
        db.newTransaction().use { transactions ->
            db.update(DbSchema.ShoppingList.TABLE, ShoppingListValueProvider(list).toContentValues(), "${DbSchema.ShoppingList.ID}=?", list.id.toString())
            transactions.markSuccessful()
        }
    }

    fun archiveList(item: ShoppingListModel) {
        db.newTransaction().use { transactions ->
            db.update(DbSchema.ShoppingList.TABLE, ShoppingListValueProvider(item).toContentValues(), "${DbSchema.ShoppingList.ID}=?", item.id.toString())
            transactions.markSuccessful()
        }
    }
}