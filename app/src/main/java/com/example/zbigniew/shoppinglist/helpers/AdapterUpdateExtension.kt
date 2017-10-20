package com.example.zbigniew.shoppinglist.helpers

import android.support.v7.widget.RecyclerView

fun <T> RecyclerView.Adapter<out RecyclerView.ViewHolder>.replaceData(adapterItems: MutableList<T>, newItems: Collection<T>, offset: Int = 0) {

    newItems.forEachIndexed { newPosition, newItem ->
        val oldPosition = adapterItems.indexOf(newItem, newPosition)
        when {
            oldPosition == -1 -> { //insert new item
                adapterItems.add(newPosition, newItem)
                notifyItemInserted(newPosition + offset)
            }
            oldPosition != newPosition -> { //move existing item
                adapterItems.removeAt(oldPosition)
                adapterItems.add(newPosition, newItem)
                notifyItemMoved(oldPosition + offset, newPosition + offset)
            }
        //or else the item is at appropriate position and we don;t have to do anything about it
        }
    }

    //now the excessive items are at the end of the list
    for (i in adapterItems.size - 1 downTo newItems.size) {
        adapterItems.removeAt(i)
        notifyItemRemoved(i + offset)
    }

}

/** Find first occurrence of [item] starting from [fromIndex]*/
private fun <E> List<E>.indexOf(item: E, fromIndex: Int): Int {
    for (i in fromIndex until this.size) {
        if (this[i] == item) return i
    }
    return -1
}