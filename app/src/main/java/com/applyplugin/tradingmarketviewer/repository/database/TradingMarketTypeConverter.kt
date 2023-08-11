package com.applyplugin.tradingmarketviewer.repository.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class TradingMarketTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun tradingMarketResponseToString(tradingMarketResponse: List<TradingMarketResponse>): String{
        return gson.toJson(tradingMarketResponse)
    }

    @TypeConverter
    fun stringToTradingMarketResponse(data: String): List<TradingMarketResponse>{
        var listType = object : TypeToken<List<TradingMarketResponse>>(){}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun tradingMarketItemToString(tradingMarketResponse: TradingMarketResponse): String{
        return gson.toJson(tradingMarketResponse)
    }

    @TypeConverter
    fun stringToTradingMarketItem(data: String): TradingMarketResponse{
        var type = object : TypeToken<TradingMarketResponse>(){}.type
        return gson.fromJson(data, type)
    }
}