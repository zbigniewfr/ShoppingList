package com.example.zbigniew.shoppinglist.view.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zbigniew.shoppinglist.R
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import com.example.zbigniew.shoppinglist.helpers.ListClickListener


class ShoppingListsAdapter(context: Context) : RecyclerView.Adapter<ShoppingListItemViewHolder>() {

    private var shoppingLists: MutableList<ShoppingListModel> = mutableListOf()
    lateinit var clickListener: ListClickListener<ShoppingListModel>

    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        holder.bind(shoppingLists[position], position)
    }

    override fun getItemCount(): Int = shoppingLists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ShoppingListItemViewHolder(inflater.inflate(R.layout.shopping_list_item_view_holder, parent, false))
        view.itemClickListener = clickListener
        return view
    }

    fun setData(newShoppingLists: List<ShoppingListModel>) {
        shoppingLists.clear()
        shoppingLists.addAll(newShoppingLists)
        notifyDataSetChanged()
    }

    fun addItem(newShoppingList: ShoppingListModel) {
        shoppingLists.add(0, newShoppingList)
        notifyItemInserted(0)
    }

    fun removeList(position: Int) {
        shoppingLists.removeAt(position)
        notifyItemRemoved(position)
    }

}