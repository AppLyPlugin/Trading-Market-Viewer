package com.applyplugin.tradingmarketviewer.util

import androidx.recyclerview.widget.DiffUtil
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse

class TradingMarketDiffUtil(
    private val oldList: List<TradingMarketResponse>,
    private val newList: List<TradingMarketResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}