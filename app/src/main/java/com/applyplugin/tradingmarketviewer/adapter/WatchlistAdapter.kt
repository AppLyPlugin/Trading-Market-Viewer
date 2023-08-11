package com.applyplugin.tradingmarketviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.tradingmarketviewer.databinding.WatchlistRowLayoutBinding
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity
import com.applyplugin.tradingmarketviewer.util.TradingMarketDiffUtil

class WatchlistAdapter: RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    private var watchlist = emptyList<WatchlistEntity>()

    class ViewHolder(private val binding: WatchlistRowLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(watchlistEntity: WatchlistEntity){
            binding.watchlist = watchlistEntity
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WatchlistRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val selectedWatchlist = watchlist[position]
        holder.bind(selectedWatchlist)

    }

    override fun getItemCount(): Int {
        return watchlist.size
    }

    fun setData(newData: List<WatchlistEntity>) {
        val watchlistMarketDiffUtil = TradingMarketDiffUtil(watchlist, newData)
        val diffUtilResult = DiffUtil.calculateDiff(watchlistMarketDiffUtil)
        watchlist = newData
        //notifyDataSetChanged()
        diffUtilResult.dispatchUpdatesTo(this)
    }
}