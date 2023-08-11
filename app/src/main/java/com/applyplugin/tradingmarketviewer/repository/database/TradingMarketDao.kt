package com.applyplugin.tradingmarketviewer.repository.database

import androidx.room.*
import com.applyplugin.tradingmarketviewer.repository.database.entities.TradingMarketEntity
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradingMarketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradingMarketData(tradingMarketEntity: TradingMarketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlistEntity: WatchlistEntity)

    @Query("SELECT * FROM TRADINGMARKETVIEW_TABLE ORDER BY id ASC")
    fun readTradingMarketData(): Flow<List<TradingMarketEntity>>

    @Query("SELECT * FROM WATCHLIST_TABLE ORDER BY id ASC")
    fun readWatchlist(): Flow<List<WatchlistEntity>>

    @Delete
    suspend fun deleteWatchlist(watchlistEntity: WatchlistEntity)

    @Query("DELETE FROM WATCHLIST_TABLE")
    suspend fun deleteAllWatchlist()

}