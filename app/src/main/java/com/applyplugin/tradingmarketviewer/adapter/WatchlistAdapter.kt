package com.applyplugin.tradingmarketviewer.adapter

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.tradingmarketviewer.R
import com.applyplugin.tradingmarketviewer.databinding.WatchlistRowLayoutBinding
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity
import com.applyplugin.tradingmarketviewer.ui.fragments.watchlist.WatchlistFragmentDirections
import com.applyplugin.tradingmarketviewer.util.TradingMarketDiffUtil
import com.applyplugin.tradingmarketviewer.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class WatchlistAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiselection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedWatchlist = arrayListOf<WatchlistEntity>()
    private var myViewHolders = arrayListOf<ViewHolder>()
    private var watchlist = emptyList<WatchlistEntity>()

    class ViewHolder(val binding: WatchlistRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(watchlistEntity: WatchlistEntity) {
            binding.watchlist = watchlistEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentWatchlist = watchlist[position]
        holder.bind(currentWatchlist)

        /**Single Click Listener**/
        holder.binding.watchlistRowLayout.setOnClickListener {
            if (multiselection) {
                applySelection(holder, currentWatchlist)
            } else {
                val action =
                    WatchlistFragmentDirections.actionWatchFragmentToDetailsActivity(
                        currentWatchlist.data
                    )

                holder.itemView.findNavController().navigate(action)
            }
        }

        /**Long Click Listener**/
        holder.binding.watchlistRowLayout.setOnLongClickListener {
            if (!multiselection) {
                multiselection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentWatchlist)
                true
            } else {
                multiselection = false
                false
            }
        }

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

    private fun applySelection(holder: ViewHolder, currentWatchlist: WatchlistEntity) {
        if (selectedWatchlist.contains(currentWatchlist)) {
            selectedWatchlist.remove(currentWatchlist)
            changeWatchlistStyle(holder, R.color.cardview_light_background, R.color.lightGray)
            applyActionModeTitle()
        }else{
            selectedWatchlist.add(currentWatchlist)
            changeWatchlistStyle(holder, R.color.dark, R.color.darker)
            applyActionModeTitle()
        }
    }

    private fun changeWatchlistStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.tradingmarketLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.watchlistCardview.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyStatusColor(color: Int) {
        requireActivity.window.statusBarColor = (
                ContextCompat.getColor(requireActivity, color)
                )

    }

    private fun applyActionModeTitle(){
        when(selectedWatchlist.size){
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedWatchlist.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedWatchlist.size} items selected"
            }
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.contextual_action, menu)
        mActionMode = mode!!
        applyStatusColor(R.color.contextualModeColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_watchlist){
            selectedWatchlist.forEach{
                mainViewModel.deleteWatchlist(it)
            }
            showSnackbar("${selectedWatchlist.size} item/s removed")
            multiselection = false
            selectedWatchlist.clear()
            mActionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach{holder ->
            changeWatchlistStyle(holder, R.color.cardview_light_background, R.color.lightGray)
        }

        multiselection = false
        selectedWatchlist.clear()
        applyStatusColor(R.color.statusBarColor)
    }

    private fun showSnackbar(message: String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK"){}
            .show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}