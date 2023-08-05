package com.applyplugin.tradingmarketviewer.util

class Constants {

    companion object {
        val BASE_URL: String = "https://api.coingecko.com/"
        val API_DELAY: Long = 30000

        val currency = "usd"
        val order = "market_cap_desc"
        val per_page = "20"
        val page = "1"
        val sparkline= false
        val locale = "en"

        //Query Keys
        const val QUERY_CURRENCY = "vs_currency"
        const val QUERY_ORDER = "order"
        const val QUERY_PER_PAGE = "per_page"
        const val QUERY_PAGE = "page"
        const val QUERY_SPARKLINE = "sparkline"
        const val QUERY_LOCALE = "locale"

        //ROOM Database
        const val TRADINGMARKETVIEW_DB = "tradingmarketview_database"
        const val TRADINGMARKETVIEW_TABLE = "tradingmarketview_table"

    }
}