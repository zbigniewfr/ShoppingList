package com.example.zbigniew.shoppinglist.helpers

interface ItemClickListener<in T> {
    fun onItemChecked(item: T)
    fun onItemDeleted(item: T, position: Int)
}