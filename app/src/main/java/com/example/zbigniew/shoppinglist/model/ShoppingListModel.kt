package com.example.zbigniew.shoppinglist.model

data class ShoppingListModel(
        val id: Long,
        val name: String,
        val createDate: Long,
        var isArchived: Boolean = false,
        val itemList: MutableList<ItemModel>,
        var checkedItemsCount: Int,
        var allItemsCount: Int
)