package com.example.zbigniew.shoppinglist.view.details

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zbigniew.shoppinglist.R
import com.example.zbigniew.shoppinglist.helpers.ItemClickListener
import com.example.zbigniew.shoppinglist.helpers.replaceData
import com.example.zbigniew.shoppinglist.model.ItemModel

class ListDetailsAdapter(context: Context, val isArchived: Boolean) : RecyclerView.Adapter<ItemViewHolder>() {

    private var items: MutableList<ItemModel> = mutableListOf()
    lateinit var clickListener: ItemClickListener<ItemModel>

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], position, isArchived)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemViewHolder(inflater.inflate(R.layout.item_view_holder, parent, false))
        view.itemClickListener = clickListener
        return view
    }

    fun setData(newItems: List<ItemModel>) = replaceData(items, newItems)

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

}