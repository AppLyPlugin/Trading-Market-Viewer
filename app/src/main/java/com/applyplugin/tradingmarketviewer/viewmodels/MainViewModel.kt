package com.applyplugin.tradingmarketviewer.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.repository.Repository
import com.applyplugin.tradingmarketviewer.repository.database.TradingMarketEntity
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.API_DELAY
import com.applyplugin.tradingmarketviewer.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /************ ROOM DATABASE ************/

    val readTradingMarket: LiveData<List<TradingMarketEntity>> =
        repository.localSource.readDatabase().asLiveData()

    private fun insertTradingMarketData(tradingMarketEntity: TradingMarketEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localSource.insertTradingMarketData(tradingMarketEntity)
        }

    /************ RETROFIT ************/

    var tradingMarketResponse: MutableLiveData<NetworkResult<List<TradingMarketResponse>>> =
        MutableLiveData()
    var searchedTradingMarketResponse: MutableLiveData<NetworkResult<List<TradingMarketResponse>>> =
        MutableLiveData()

    fun getTradingMarketData(query: HashMap<String, String>) = viewModelScope.launch {
        callTradingMarket(query)
    }

    private var job: Job? = null

    fun searchData(searchQuery: HashMap<String, String>) = viewModelScope.launch {
        searchTradingMarket(searchQuery)
    }

    private suspend fun callTradingMarket(query: HashMap<String, String>) {
        tradingMarketResponse.value = NetworkResult.Loading()
        job = viewModelScope.launch {
            while (true) {
                if (hasInternetConnection()) {
                    Log.d("MainViewModel", "Has Internet Connection!")
                    try {
                        val response = repository.remoteSource.getTradingMarket(query)
                        tradingMarketResponse.value = handleTradingMarketResponse(response)
                        val tradingMarketData = tradingMarketResponse.value!!.data
                        if (tradingMarketData != null) {
                            offlineCacheTradingMarketData(tradingMarketData)
                        }
                    } catch (e: Exception) {
                        tradingMarketResponse.value = NetworkResult.Error("Error API Limitation")
                        e.stackTrace
                    }
                } else {
                    Log.d("MainViewModel", "No Internet Connection!")
                    tradingMarketResponse.value = NetworkResult.Error("No Internet Connection")
                }
                delay(API_DELAY)
            }
        }
    }

    private suspend fun searchTradingMarket(searchQuery: HashMap<String, String>) {
        tradingMarketResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            Log.d("MainViewModel", "Has Internet Connection!")
            try {
                val response = repository.remoteSource.searchTradingMarket(searchQuery)
                tradingMarketResponse.value = handleTradingMarketResponse(response)
            } catch (e: Exception) {
                tradingMarketResponse.value = NetworkResult.Error("Error API Limitation")
                e.stackTrace
            }
        } else {
            Log.d("MainViewModel", "No Internet Connection!")
            tradingMarketResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheTradingMarketData(tradingMarketData: List<TradingMarketResponse>) {

        val tradingMarketEntity = TradingMarketEntity(tradingMarketData)
        insertTradingMarketData(tradingMarketEntity)

    }

    private fun handleTradingMarketResponse(response: Response<List<TradingMarketResponse>>):
            NetworkResult<List<TradingMarketResponse>>? {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Connection Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("Network Call Limited")
            }

            response.body()!!.toString().isNullOrEmpty() -> {
                return NetworkResult.Error("Data Not Found")
            }

            response.code() == 200 ||
                    response.isSuccessful -> {
                val tradingMarketData = response.body()
                return NetworkResult.Success(tradingMarketData!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        job = null
    }
}