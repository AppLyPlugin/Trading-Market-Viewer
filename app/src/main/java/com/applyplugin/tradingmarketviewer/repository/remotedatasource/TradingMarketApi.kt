package com.applyplugin.tradingmarketviewer.repository.remotedatasource

import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TradingMarketApiInterface {

    @GET("api/v3/coins/markets")
    suspend fun getTradingMarket(
        @QueryMap queries: HashMap<String, String>
    ): Response<List<TradingMarketResponse>>

}