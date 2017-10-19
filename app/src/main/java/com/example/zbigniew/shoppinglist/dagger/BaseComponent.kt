package com.example.zbigniew.shoppinglist.dagger

import com.example.zbigniew.shoppinglist.view.details.ListDetailsActivity
import com.example.zbigniew.shoppinglist.view.list.ShoppingListActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(BaseSystemModule::class, DataModule::class))
interface BaseComponent {
    fun inject(view: ShoppingListActivity)
    fun inject(view: ListDetailsActivity)
}