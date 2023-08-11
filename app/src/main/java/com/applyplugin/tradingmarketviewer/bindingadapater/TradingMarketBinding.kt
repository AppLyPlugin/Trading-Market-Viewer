package com.applyplugin.tradingmarketviewer.bindingadapater

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.repository.database.entities.TradingMarketEntity
import com.applyplugin.tradingmarketviewer.util.NetworkResult

class TradingMarketBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            view: View,
            apiResponse: NetworkResult<List<TradingMarketResponse>>?,
            database: List<TradingMarketEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                view.visibility = View.VISIBLE
                when(view){
                    is TextView -> view.text = apiResponse.message.toString()
                }
            }else{
                view.visibility = View.INVISIBLE
            }
        }
    }
}