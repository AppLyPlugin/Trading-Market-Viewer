package com.applyplugin.tradingmarketviewer.repository

import com.applyplugin.tradingmarketviewer.repository.database.TradingMarketDao
import com.applyplugin.tradingmarketviewer.repository.database.TradingMarketEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val tradingMarketDao: TradingMarketDao
) {

    fun readDatabase(): Flow<List<TradingMarketEntity>> {
        return tradingMarketDao.readTradingMarketData()
    }

    suspend fun insertTradingMarketData(tradingMarketEntity: TradingMarketEntity){
        tradingMarketDao.insertTradingMarketData(tradingMarketEntity)
    }

}