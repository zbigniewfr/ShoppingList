package com.example.zbigniew.shoppinglist.view.list

import com.example.zbigniew.shoppinglist.helpers.applyTransformerFlowable
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import com.example.zbigniew.shoppinglist.repository.local.LocalDataSource
import com.example.zbigniew.shoppinglist.view.list.ShoppingListActivity.Mode
import com.futuremind.mvpbase.MvpView
import com.futuremind.mvpbase.RxBasePresenter
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject


class ShoppingListsPresenter
@Inject
constructor(
        private val db: LocalDataSource
) : RxBasePresenter<ShoppingListsPresenter.View>() {

    var mode: Mode = Mode.NORMAL

    fun addDisposable(disposable: Disposable) {
        disposable.registerInPresenter()
    }

    fun loadShoppingLists() {
        when (mode) {
            Mode.NORMAL -> loadLists()
            Mode.ARCHIVE -> loadArchivedLists()
        }
    }

    private fun loadArchivedLists() {
        db.getAllArchivedLists()
                .compose(applyTransformerFlowable())
                .subscribe({
                    view?.showShoppingLists(it)
                }, {
                    it.printStackTrace()
                }).registerInPresenter()
    }

    private fun loadLists() {
        db.getAllLists()
                .map { it.sortedWith(compareByDescending({it.createDate})).toMutableList() }
                .compose(applyTransformerFlowable())
                .subscribe({
                    view?.showShoppingLists(it)
                }, {
                    it.printStackTrace()
                }).registerInPresenter()
    }

    fun addList(listName: String) {
        val shoppingList = ShoppingListModel(-1, listName, Date().time, false, mutableListOf(), 0, 0)
        val id = db.saveShoppingList(shoppingList)
        db.getListById(id)
                .subscribe(
                        {
                            view?.addList(it)
                        }, {
                    it.printStackTrace()
                })
                .registerInPresenter()
    }

    fun archiveList(item: ShoppingListModel) {
        db.archiveList(item)
    }

    interface View : MvpView {
        fun showShoppingLists(shoppingLists: MutableList<ShoppingListModel>)
        fun addList(listModel: ShoppingListModel?)
    }
}