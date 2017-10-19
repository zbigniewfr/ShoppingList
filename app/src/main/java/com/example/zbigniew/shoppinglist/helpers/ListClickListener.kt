package com.example.zbigniew.shoppinglist.helpers

interface ListClickListener<in T> {
    fun onItemClick(item: T)
    fun onItemLongClick(item: T, position: Int)
}