package com.example.zbigniew.shoppinglist.view.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.zbigniew.shoppinglist.R
import com.example.zbigniew.shoppinglist.ShoppingListApp
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import com.example.zbigniew.shoppinglist.helpers.ListClickListener
import kotlinx.android.synthetic.main.shopping_list_activity.*
import javax.inject.Inject
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.example.zbigniew.shoppinglist.view.details.ListDetailsActivity
import com.jakewharton.rxbinding2.view.RxView
import com.afollestad.materialdialogs.MaterialDialog


class ShoppingListActivity : AppCompatActivity(), ShoppingListsPresenter.View {

    companion object {
        private const val ACTIVITY_MODE = "mode"

        fun startActivity(context: Context, mode: Mode) {
            val bundle = Bundle()
            bundle.putSerializable(ACTIVITY_MODE, mode)
            val intent = Intent(context, ShoppingListActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    enum class Mode {
        NORMAL,
        ARCHIVE
    }

    @Inject lateinit var presenter: ShoppingListsPresenter
    private lateinit var adapter: ShoppingListsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list_activity)
        ShoppingListApp.baseComponent.inject(this)
        presenter.attachView(this)
        adapter = ShoppingListsAdapter(this)
        setMode()
        setupRecycler()
    }

    private fun setMode() {
        val bundle = intent.extras
        if (bundle != null) {
            val serializable = bundle.getSerializable(ACTIVITY_MODE)
            serializable?.let {
                val mode = it as Mode
                presenter.mode = mode
            }
        }

        if (presenter.mode == Mode.ARCHIVE) {
            title = getString(R.string.archived_lists)
        }else{
            Toast.makeText(this, getString(R.string.archive_hint), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (presenter.mode == Mode.NORMAL) {
            menuInflater.inflate(R.menu.main_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.archivedLists -> {
                openArchivedListsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openArchivedListsActivity() {
        ShoppingListActivity.startActivity(this, Mode.ARCHIVE)
    }

    override fun onResume() {
        super.onResume()
        setupFab()
        presenter.loadShoppingLists()
    }

    private fun setupFab() {
        if (presenter.mode == Mode.ARCHIVE) {
            addFab.hide()
        } else {
            presenter.addDisposable(
                    RxView.clicks(addFab)
                            .subscribe({
                                openAddListDialog()
                            }, {
                                it.printStackTrace()
                            })
            )
        }
    }

    private fun openAddListDialog() {
        val wrapInScrollView = false
        MaterialDialog.Builder(this)
                .title(getString(R.string.enter_list_name))
                .customView(R.layout.add_list_dialog_layout, wrapInScrollView)
                .positiveText(getString(R.string.dialog_add_btn))
                .onPositive { dialog, _ ->
                    handleAddList(dialog)
                }
                .build()
                .show()
    }

    private fun handleAddList(dialog: MaterialDialog) {
        val editText = dialog.customView?.findViewById<EditText>(R.id.listName)
        val listName = editText?.text.toString()
        if(listName.isNotEmpty()) {
            presenter.addList(listName.toString())
        }else{
            Toast.makeText(this@ShoppingListActivity, getString(R.string.list_name_empty), Toast.LENGTH_SHORT).show()
        }
        dialog.dismiss()
    }


    private fun setupRecycler() {
        adapter.clickListener = object : ListClickListener<ShoppingListModel> {
            override fun onItemClick(item: ShoppingListModel) {
                this@ShoppingListActivity.openListDetailsActivity(item)
            }

            override fun onItemLongClick(item: ShoppingListModel, position: Int) {
                archiveList(item, position)
            }
        }
        val layoutManager = LinearLayoutManager(this)
        shoppingListsRecyclerView.setHasFixedSize(true)
        shoppingListsRecyclerView.layoutManager = layoutManager
        shoppingListsRecyclerView.adapter = adapter
    }

    private fun archiveList(item: ShoppingListModel, position: Int) {
        if (presenter.mode == Mode.NORMAL) {
            presenter.archiveList(item)
            adapter.removeList(position)
            Toast.makeText(this@ShoppingListActivity, getString(R.string.list_archived), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openListDetailsActivity(item: ShoppingListModel) {
        ListDetailsActivity.startActivity(this, item.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showShoppingLists(shoppingLists: MutableList<ShoppingListModel>) {
        adapter.setData(shoppingLists)
    }
}
