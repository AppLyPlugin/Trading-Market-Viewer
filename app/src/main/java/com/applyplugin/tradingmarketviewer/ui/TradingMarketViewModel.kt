package com.applyplugin.tradingmarketviewer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_CURRENCY
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_LOCALE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_ORDER
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_PAGE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_PER_PAGE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_SPARKLINE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.currency
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.locale
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.order
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.page
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.per_page
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.sparkline

class TradingMarketViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQuery(): HashMap<String, String> {

        val queries : HashMap<String, String> = HashMap()

        queries[QUERY_CURRENCY] = currency
        queries[QUERY_ORDER] = order
        queries[QUERY_PER_PAGE] = per_page
        queries[QUERY_PAGE] = page
        queries[QUERY_SPARKLINE] = sparkline.toString()
        queries[QUERY_LOCALE] = locale

        while(queries.values.removeIf{it == ""}){}

        return queries
    }
}