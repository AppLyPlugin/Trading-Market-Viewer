package com.applyplugin.tradingmarketviewer.bindingadapater

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.tradingmarketviewer.adapter.WatchlistAdapter
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity

class WatchlistBinding {

    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun erroView(
            view: View,
            database: List<WatchlistEntity>?,
            mAdapter: WatchlistAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    var dataBaseCheck = database.isNullOrEmpty()
                    view.isInvisible = dataBaseCheck

                    if (!dataBaseCheck) {
                        database?.let { mAdapter?.setData(it) }
                    }
                }

                else -> view.isVisible = database.isNullOrEmpty()
            }

        }
    }
}
