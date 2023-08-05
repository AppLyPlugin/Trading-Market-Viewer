package com.applyplugin.tradingmarketviewer.repository.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TradingMarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradingMarketData(tradingMarketEntity: TradingMarketEntity)

    @Query("SELECT * FROM TRADINGMARKETVIEW_TABLE ORDER BY id ASC")
    fun readTradingMarketData(): Flow<List<TradingMarketEntity>>

}