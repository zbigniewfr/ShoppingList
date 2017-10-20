package com.example.zbigniew.shoppinglist.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.example.zbigniew.shoppinglist.R
import com.example.zbigniew.shoppinglist.ShoppingListApp
import com.example.zbigniew.shoppinglist.helpers.ItemClickListener
import com.example.zbigniew.shoppinglist.model.ItemModel
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.shopping_list_activity.*
import javax.inject.Inject

class ListDetailsActivity : AppCompatActivity(), ListDetailsPresenter.View {

    companion object {

        private const val LIST_ID_KEY = "list_id"
        private const val LIST_NAME_KEY = "list_name"

        fun startActivity(context: Context, listId: Long) {
            val bundle = Bundle()
            bundle.putLong(LIST_ID_KEY, listId)
            val intent = Intent(context, ListDetailsActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    @Inject lateinit var presenter: ListDetailsPresenter
    private lateinit var adapter: ListDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list_activity)
        ShoppingListApp.baseComponent.inject(this)
        presenter.attachView(this)
        setupPresenter()
    }

    private fun setupPresenter() {
        val bundle = intent.extras
        val listId = bundle.getLong(LIST_ID_KEY)
        presenter.listId = listId
        presenter.getList()
        presenter.loadShoppingListItems()
    }

    override fun setupView() {
        setupRecycler()
        if (presenter.shoppingList.isArchived) {
            addFab.hide()
        } else {
            presenter.addDisposable(
                    RxView.clicks(addFab)
                            .subscribe({
                                openAddItemDialog()
                            }, {
                                it.printStackTrace()
                            })
            )
        }
    }

    override fun setTitle(name: String) {
        title = name
    }

    private fun openAddItemDialog() {
        val wrapInScrollView = false
        MaterialDialog.Builder(this)
                .title(getString(R.string.enter_item_name))
                .customView(R.layout.add_item_dialog_layout, wrapInScrollView)
                .positiveText(getString(R.string.dialog_add_btn))
                .onPositive { dialog, _ -> handleDialogClose(dialog) }
                .build()
                .show()
    }

    private fun handleDialogClose(dialog: MaterialDialog) {
        val editText = dialog.customView?.findViewById<EditText>(R.id.itemName)
        val itemName = editText?.text.toString()
        presenter.addItem(itemName)
        dialog.dismiss()
    }

    private fun setupRecycler() {
        adapter = ListDetailsAdapter(this, presenter.shoppingList.isArchived)
        adapter.clickListener = object : ItemClickListener<ItemModel> {

            override fun onItemChecked(item: ItemModel) {
                    presenter.updateItem(item)
            }

            override fun onItemDeleted(item: ItemModel, position: Int) {
                presenter.removeItem(item)
                removeItemFromAdapter(position)
            }
        }
        val layoutManager = LinearLayoutManager(this)
        shoppingListsRecyclerView.setHasFixedSize(true)
        shoppingListsRecyclerView.layoutManager = layoutManager
        shoppingListsRecyclerView.adapter = adapter
    }

    private fun removeItemFromAdapter(position: Int) = if (!presenter.shoppingList.isArchived) {
        adapter.removeItem(position)
    } else {
        Toast.makeText(this@ListDetailsActivity, getString(R.string.cannot_remove_item), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showItems(items: MutableList<ItemModel>) {
        adapter.setData(items)
    }
}