package com.example.zbigniew.shoppinglist.repository.local

object DbSchema {

    const private val DELIMITER = "__"

    object Item {
        const val TABLE = "item"

        const val ID = TABLE + DELIMITER + "id"
        const val NAME = TABLE + DELIMITER + "name"
        const val IS_CHECKED = TABLE + DELIMITER + "is_checked"
        const val LIST_ID = TABLE + DELIMITER + "list_id"

        val CREATE_TABLE = """
        |CREATE TABLE ${TABLE} (
        | ${ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        | ${NAME} TEXT,
        | ${IS_CHECKED} INTEGER NOT NULL DEFAULT 0,
        | ${LIST_ID} INTEGER NOT NULL REFERENCES ${ShoppingList.TABLE}(${ShoppingList.ID}))""".trimMargin()
    }


    object ShoppingList {
        const val TABLE = "shopping_list"

        const val ID = TABLE + DELIMITER + "id"
        const val NAME = TABLE + DELIMITER + "name"
        const val ARCHIVED = TABLE + DELIMITER + "is_archived"
        const val CHECKED_ITEMS = TABLE + DELIMITER + "checked_items"
        const val ALL_ITEMS = TABLE + DELIMITER + "all_items"
        const val CREATE_DATE = TABLE + DELIMITER + "create_date"

        val CREATE_TABLE = """
        |CREATE TABLE ${TABLE} (
        | ${ID} INTEGER PRIMARY KEY AUTOINCREMENT,
        | ${NAME} TEXT,
        | ${ShoppingList.ARCHIVED} INTEGER NOT NULL DEFAULT 0,
        | ${CHECKED_ITEMS} INTEGER,
        | ${ALL_ITEMS} INTEGER,
        | ${CREATE_DATE} NUMERIC)""".trimMargin()
    }

    object Index {

        val CREATE_INDEX = "CREATE INDEX item_list_id ON " + Item.TABLE + " (" + Item.LIST_ID + ")";
    }
}
