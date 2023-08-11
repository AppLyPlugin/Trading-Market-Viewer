package com.applyplugin.tradingmarketviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.applyplugin.tradingmarketviewer.databinding.ActivityDetailsBinding
import com.applyplugin.tradingmarketviewer.repository.database.entities.WatchlistEntity
import com.applyplugin.tradingmarketviewer.ui.fragments.details.DetailsFragment
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.TRADINGMARKET_BUNDLE
import com.applyplugin.tradingmarketviewer.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val mainViewModel: MainViewModel by viewModels()

    private val args by navArgs<DetailsActivityArgs>()

    private var watchlistSaved = false
    private var savedWatchlistId = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.title = args.result.name.uppercase()

        val bundleData = Bundle()
        bundleData.putParcelable(TRADINGMARKET_BUNDLE, args.result)
        binding.fragmentContainerView.getFragment<DetailsFragment>().arguments = bundleData

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_watchlist_menu)
        checkWatchlistData(menuItem!!)
        return true
    }

    private fun checkWatchlistData(menuItem: MenuItem) {
        mainViewModel.readWatchlist.observe(this){ watchlistEntity ->
            try{
                for(watchlistItem in watchlistEntity){
                    if(watchlistItem.data.id == args.result.id){
                        changeMenuItemColor(menuItem, R.color.yellow)
                        watchlistSaved = true;
                        break;
                    }else{
                        changeMenuItemColor(menuItem, R.color.white)
                        watchlistSaved = false;
                    }
                }
            }catch(e: Exception){
                Log.d("DetailsActivity.kt", e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }else if(item.itemId == R.id.save_to_watchlist_menu && !watchlistSaved){
            saveToWatchlist(item)
        }else if(item.itemId == R.id.save_to_watchlist_menu && watchlistSaved){
            deleteFromWatchlist(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveToWatchlist(item: MenuItem) {
        val watchlistEntity =
            WatchlistEntity(
                args.result
            )
        mainViewModel.insertWatchlist(watchlistEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Item Saved")
        watchlistSaved = true
    }

    private fun deleteFromWatchlist(item: MenuItem) {
        val watchlistEntity =
            WatchlistEntity(
                args.result
            )
        mainViewModel.deleteWatchlist(watchlistEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Deleted from Watchlist")
        watchlistSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }
}