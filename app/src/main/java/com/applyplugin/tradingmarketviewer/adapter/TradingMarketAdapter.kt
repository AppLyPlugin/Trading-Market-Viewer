package com.applyplugin.tradingmarketviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.tradingmarketviewer.databinding.TradingmarketRowLayoutBinding
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.util.TradingMarketDiffUtil

class TradingMarketAdapter : RecyclerView.Adapter<TradingMarketAdapter.MyViewHolder>() {

    private var tradingMarketResponse = emptyList<TradingMarketResponse>()

    class MyViewHolder(private val binding: TradingmarketRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tradingMarketResponse: TradingMarketResponse) {
            binding.result = tradingMarketResponse
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TradingmarketRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = tradingMarketResponse[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return tradingMarketResponse.size
    }

    fun setData(newData: List<TradingMarketResponse>) {
        val tradingMarketDiffUtil = TradingMarketDiffUtil(tradingMarketResponse, newData)
        val diffUtilResult = DiffUtil.calculateDiff(tradingMarketDiffUtil)
        tradingMarketResponse = newData
        //notifyDataSetChanged()
        diffUtilResult.dispatchUpdatesTo(this)
    }
}