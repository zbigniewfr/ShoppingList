package com.example.zbigniew.shoppinglist

import android.app.Application
import com.example.zbigniew.shoppinglist.dagger.BaseComponent
import com.example.zbigniew.shoppinglist.dagger.BaseSystemModule
import com.example.zbigniew.shoppinglist.dagger.DaggerBaseComponent
import com.example.zbigniew.shoppinglist.dagger.DataModule

class ShoppingListApp : Application(){

    companion object {
        lateinit var baseComponent: BaseComponent
    }

    override fun onCreate() {
        super.onCreate()
        baseComponent = DaggerBaseComponent.builder()
                .baseSystemModule(BaseSystemModule(this))
                .dataModule(DataModule())
                .build()
    }

}