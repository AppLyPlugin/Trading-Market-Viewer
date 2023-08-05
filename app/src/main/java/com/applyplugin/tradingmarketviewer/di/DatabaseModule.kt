package com.applyplugin.tradingmarketviewer.di

import android.content.Context
import androidx.room.Room
import com.applyplugin.tradingmarketviewer.repository.database.TradingMarketDatabase
import com.applyplugin.tradingmarketviewer.repository.database.TradingMarketTypeConverter
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.TRADINGMARKETVIEW_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TradingMarketDatabase::class.java,
        TRADINGMARKETVIEW_DB
    ).addTypeConverter(TradingMarketTypeConverter())
        .build()


    @Singleton
    @Provides
    fun provideDao(database: TradingMarketDatabase) = database.tradingMarketDao()

}