package com.applyplugin.tradingmarketviewer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TradingMarketViewerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}