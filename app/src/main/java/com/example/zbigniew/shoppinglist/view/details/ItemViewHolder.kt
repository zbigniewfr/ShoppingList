package com.example.zbigniew.shoppinglist.view.details

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.zbigniew.shoppinglist.helpers.ItemClickListener
import com.example.zbigniew.shoppinglist.helpers.ListClickListener
import com.example.zbigniew.shoppinglist.model.ItemModel
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.item_view_holder.view.*

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var itemClickListener: ItemClickListener<ItemModel>

    @SuppressLint("RxLeakedSubscription")
    fun bind(item: ItemModel, position: Int) {
        itemView.itemName.text = item.name
        itemView.checkbox.isChecked = item.isChecked
        RxView.clicks(itemView.deleteItemIv)
                .subscribe({
                    itemClickListener.onItemDeleted(item, position)
                },{
                    it.printStackTrace()
                })
        itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            item.isChecked = isChecked
            itemClickListener.onItemChecked(item)
        }
    }
}