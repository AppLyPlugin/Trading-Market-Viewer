package com.applyplugin.tradingmarketviewer.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.TRADINGMARKETVIEW_TABLE

@Entity(tableName = TRADINGMARKETVIEW_TABLE)
class TradingMarketEntity(
    var tradingMarket: List<TradingMarketResponse>
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}