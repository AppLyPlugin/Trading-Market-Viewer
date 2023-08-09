package com.applyplugin.tradingmarketviewer.util

class Constants {

    companion object {
        val BASE_URL: String = "https://api.coingecko.com/"
        val API_DELAY: Long = 30000

        //QUERY
        val PER_PAGE = "20"
        val PAGE = "1"
        val SPARKLINE = false
        val LOCALE = "en"

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

        //Bottom Sheep and Pref
        const val PREF_NAME = "tradingmarket_preferences"
        const val DEFAULT_CURRENCY = "usd"
        const val DEFAULT_ORDER = "market_cap_desc"
        const val CURRENCY = "currency"
        const val CURRENCY_ID = "currency_id"
        const val ORDER = "order"
        const val ORDER_ID = "order_id"
        const val BACK_ONLINE = "backOnline"
        const val QUERY_SEARCH_ID = "ids"

        //Details Activity Bundle
        const val TRADINGMARKET_BUNDLE = "tradingmarket_bundle"
        const val BUNDLE_MARKETCAP_PRICE = "market_cap"
        const val BUNDLE_MARKETCAP_CHANGE = "market_cap_change"
        const val BUNDLE_ATH_PRICE = "ath"
        const val BUNDLE_ATH_CHANGE = "ath_change"
        const val BUNDLE_ATH_DATE = "ath_date"
        const val BUNDLE_ATL_PRICE = "atl"
        const val BUNDLE_ATL_CHANGE = "atl_change"
        const val BUNDLE_ATL_DATE = "atl_date"
        const val TITLE_MARKETCAP = "Market Cap"
        const val TITLE_ATH = "All Time High"
        const val TITLE_ATL = "All Time Low"

    }

    enum class Currency(val currency: String) {
        //TODO Add more currencies
        USD("usd"),
        EUR("eur"),
        JPY("jpy")
    }

    enum class Locale(val locale: String){
        AR("ar"),
        BG("bg"),
        CS("cs"),
        DA("da"),
        DE("de"),
        EL("el"),
        EN("en"),
        ES("es"),
        FI("fi"),
        FR("fr"),
        HE("he"),
        HI("hi"),
        HR("hr"),
        HU("hu"),
        ID("id"),
        IT("it"),
        JA("ja"),
        KO("ko"),
        LT("lt"),
        NL("nl"),
        NO("no"),
        PL("pl"),
        PT("pt"),
        RO("ro"),
        RU("ru"),
        SK("sk"),
        SL("sl"),
        SV("sv"),
        TH("th"),
        TR("tr"),
        UK("uk"),
        VI("vi"),
        ZH("zh")
    }

    enum class Order(val order: String){
        MarketCapDesc("market_cap_desc"),
        MarketCapAsc("market_cap_asc"),
        NameDesc("name_desc"),
        NameAsc("name_asc"),
        MarketCapChange24hDesc("market_cap_change_24h_desc"),
        MarketCapChange24hAsc("market_cap_change_24h_asc")
    }

}