package com.applyplugin.tradingmarketviewer.repository.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.WATCHLIST_TABLE

@Entity(tableName = WATCHLIST_TABLE)
class WatchlistEntity(
    var data: TradingMarketResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: String = data.symbol
}