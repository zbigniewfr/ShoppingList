package com.example.zbigniew.shoppinglist.view.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.zbigniew.shoppinglist.model.ShoppingListModel
import com.example.zbigniew.shoppinglist.helpers.ListClickListener
import kotlinx.android.synthetic.main.shopping_list_item_view_holder.view.*

class ShoppingListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var itemClickListener: ListClickListener<ShoppingListModel>

    fun bind(list: ShoppingListModel, position: Int) {
        itemView.listName.text = list.name
        itemView.checkedItemsCount.text = list.checkedItemsCount.toString()
        itemView.allItemsCount.text = list.allItemsCount.toString()
        itemView.setOnClickListener {
            itemClickListener.onItemClick(list)
        }
        itemView.setOnLongClickListener {
            list.isArchived = true
            itemClickListener.onItemLongClick(list, position)
            true
        }
    }
}