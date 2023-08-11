package com.applyplugin.tradingmarketviewer.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.applyplugin.tradingmarketviewer.di.DatabaseModule.provideDatabase
import com.applyplugin.tradingmarketviewer.repository.database.entities.TradingMarketEntity
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity

@Database(
    entities = [TradingMarketEntity::class, WatchlistEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TradingMarketTypeConverter::class)
abstract class TradingMarketDatabase: RoomDatabase() {

    abstract fun tradingMarketDao(): TradingMarketDao

    companion object{
        @Volatile
        private var instance: TradingMarketDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: provideDatabase(context).also{ instance = it}
        }

    }

}