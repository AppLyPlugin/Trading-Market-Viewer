package com.applyplugin.tradingmarketviewer.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.applyplugin.tradingmarketviewer.repository.DataStoreRepository
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.CURRENCY
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_CURRENCY
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_LOCALE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_ORDER
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_PAGE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_PER_PAGE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_SPARKLINE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.DEFAULT_CURRENCY
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.DEFAULT_ORDER
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.LOCALE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.ORDER
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.PAGE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.PER_PAGE
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.QUERY_SEARCH_ID
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.SPARKLINE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradingMarketViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var currency = CURRENCY
    private var order = ORDER

    var networkStatus = false
    var backOnline = false

    val readTradingMarketFilter = dataStoreRepository.readTradingViewFilter
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveTradingMarketFilter(currency: String, currencyId: Int, order: String, orderId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveTradingViewFilter(currency, currencyId, order, orderId)
        }

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readTradingMarketFilter.collect { value ->
                currency = value.selectedCurrency
                order = value.selectedOrder

            }
        }

        queries[QUERY_CURRENCY] = currency
        queries[QUERY_ORDER] = order
        queries[QUERY_PER_PAGE] = PER_PAGE
        queries[QUERY_PAGE] = PAGE
        queries[QUERY_SPARKLINE] = SPARKLINE.toString()
        queries[QUERY_LOCALE] = LOCALE

        while (queries.values.removeIf { it == "" }) {
        }

        return queries
    }
    fun applySearchQuery(searchIds: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readTradingMarketFilter.collect { value ->
                currency = value.selectedCurrency
                order = value.selectedOrder

            }
        }

        queries[QUERY_SEARCH_ID] = searchIds
        queries[QUERY_CURRENCY] = currency
        queries[QUERY_ORDER] = order
        queries[QUERY_PER_PAGE] = PER_PAGE
        queries[QUERY_PAGE] = PAGE
        queries[QUERY_SPARKLINE] = SPARKLINE.toString()
        queries[QUERY_LOCALE] = LOCALE

        while (queries.values.removeIf { it == "" }) {
        }

        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else{
            if(backOnline){
                Toast.makeText(getApplication(), "We're back online!", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}