package com.example.zbigniew.shoppinglist.model

data class ItemModel(
        val id: Long,
        var name: String,
        var isChecked: Boolean,
        val listId: Long
)