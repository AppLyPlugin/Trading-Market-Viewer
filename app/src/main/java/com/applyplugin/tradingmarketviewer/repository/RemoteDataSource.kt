package com.applyplugin.tradingmarketviewer.repository

import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.repository.remotedatasource.TradingMarketApiInterface
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val tradingMarketAPIInterface: TradingMarketApiInterface) {

    suspend fun getTradingMarket(query: HashMap<String, String>): Response<List<TradingMarketResponse>>{
        return tradingMarketAPIInterface.getTradingMarket(query)
    }

}