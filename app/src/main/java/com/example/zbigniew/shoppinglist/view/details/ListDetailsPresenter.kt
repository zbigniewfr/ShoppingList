package com.example.zbigniew.shoppinglist.view.details

import android.util.Log
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import com.example.zbigniew.shoppinglist.helpers.applyTransformerFlowable
import com.example.zbigniew.shoppinglist.model.ItemModel
import com.example.zbigniew.shoppinglist.repository.local.LocalDataSource
import com.futuremind.mvpbase.MvpView
import com.futuremind.mvpbase.RxBasePresenter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ListDetailsPresenter
@Inject
constructor(
        private val db: LocalDataSource
) : RxBasePresenter<ListDetailsPresenter.View>() {

    var listId: Long = -1
    lateinit var shoppingList: ShoppingListModel

    fun addDisposable(disposable: Disposable) {
        disposable.registerInPresenter()
    }

    fun loadShoppingListItems() {
        db.getItems(listId)
                .map { it.sortedWith(compareByDescending({ it.id })).toMutableList() }
                .compose(applyTransformerFlowable())
                .subscribe({
                    view?.setupView()
                    view?.showItems(it)
                }, {
                    it.printStackTrace()
                }).registerInPresenter()
    }

    interface View : MvpView {
        fun showItems(items: MutableList<ItemModel>)
        fun setTitle(name: String)
        fun setupView()
    }

    fun removeItem(item: ItemModel) {
        db.removeItem(item.id)
        if (item.isChecked) {
            shoppingList.checkedItemsCount--
        }
        shoppingList.allItemsCount--
        updateShoppingList()
    }

    fun addItem(itemName: String) {
        val item = ItemModel(-1, itemName, false, listId)
        db.addItem(item)
        shoppingList.allItemsCount++
        updateShoppingList()
    }

    fun updateItem(item: ItemModel) {
        if (!shoppingList.isArchived) {
            db.updateItem(item)
            updateCheckedCount(item)
            updateShoppingList()
        }
    }

    private fun updateCheckedCount(item: ItemModel) {
        if (item.isChecked) {
            shoppingList.checkedItemsCount++
        } else {
            shoppingList.checkedItemsCount--
        }
    }

    private fun updateShoppingList() {
        db.updateShoppingList(shoppingList)
    }

    fun getList() {
        db.getListById(listId)
                .compose(applyTransformerFlowable())
                .subscribe({
                    shoppingList = it
                    view?.setTitle(it.name)
                }, {
                    it.printStackTrace()
                })
                .registerInPresenter()
    }
}