package com.example.zbigniew.shoppinglist.repository.local


fun selectAllShoppingLists(): String {
    return """
    |SELECT * FROM ${DbSchema.ShoppingList.TABLE} WHERE ${DbSchema.ShoppingList.ARCHIVED} = 0""".trimMargin()
}

fun selectAllArchivedShoppingLists(): String {
    return """
    |SELECT * FROM ${DbSchema.ShoppingList.TABLE} WHERE ${DbSchema.ShoppingList.ARCHIVED} = 1""".trimMargin()
}

fun getShoppingListById(listId: Long): String {
    return """
    |SELECT * FROM ${DbSchema.ShoppingList.TABLE} WHERE ${DbSchema.ShoppingList.ID} = $listId""".trimMargin()
}

fun getItemById(itemId: Long): String {
    return """
    |SELECT * FROM ${DbSchema.Item.TABLE} WHERE ${DbSchema.Item.ID} = $itemId""".trimMargin()
}

fun selectItemsFromList(listId: Long): String {
    return """
    |SELECT * FROM ${DbSchema.Item.TABLE} WHERE ${DbSchema.Item.LIST_ID} = $listId""".trimMargin()
}