package com.applyplugin.tradingmarketviewer.repository

import com.applyplugin.tradingmarketviewer.repository.database.TradingMarketDao
import com.applyplugin.tradingmarketviewer.repository.database.entities.TradingMarketEntity
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(
    private val tradingMarketDao: TradingMarketDao
) {

    fun readTradingMarketDB(): Flow<List<TradingMarketEntity>> {
        return tradingMarketDao.readTradingMarketData()
    }

    fun readWatchlistDB(): Flow<List<WatchlistEntity>>{
        return tradingMarketDao.readWatchlist()
    }

    suspend fun insertTradingMarketData(tradingMarketEntity: TradingMarketEntity){
        tradingMarketDao.insertTradingMarketData(tradingMarketEntity)
    }

    suspend fun insertWatchlist(watchlistEntity: WatchlistEntity){
        tradingMarketDao.insertWatchlist(watchlistEntity)
    }

    suspend fun deleteWatchlist(watchlistEntity: WatchlistEntity){
        tradingMarketDao.deleteWatchlist(watchlistEntity)
    }

    suspend fun deleteAllWatchlist(){
        tradingMarketDao.deleteAllWatchlist()
    }

}