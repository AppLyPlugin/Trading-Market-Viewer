package com.applyplugin.tradingmarketviewer.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.applyplugin.tradingmarketviewer.util.Constants
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.DEFAULT_CURRENCY
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.DEFAULT_ORDER
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREF_NAME)

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object PreferencesKey{
        val selectedCurrency = stringPreferencesKey(Constants.CURRENCY)
        val selectedCurrencyId = intPreferencesKey(Constants.CURRENCY_ID)
        val selectedOrder = stringPreferencesKey(Constants.ORDER)
        val selectedOrderId = intPreferencesKey(Constants.ORDER_ID)
    }

    suspend fun saveTradingViewFilter(
        selectedCurrency: String,
        selectedCurrencyId: Int,
        selectedOrder: String,
        selectedOrderId: Int
    ){

        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.selectedCurrency] = selectedCurrency
            preferences[PreferencesKey.selectedCurrencyId] = selectedCurrencyId
            preferences[PreferencesKey.selectedOrder] = selectedOrder
            preferences[PreferencesKey.selectedOrderId] = selectedOrderId
        }
    }

    val readTradingViewFilter: Flow<TradingMarketFilter> = context.dataStore.data
        .catch {exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map {preferences ->
            val selectedCurrency = preferences[PreferencesKey.selectedCurrency] ?: DEFAULT_CURRENCY
            val selectedCurrencyId = preferences[PreferencesKey.selectedCurrencyId] ?: 0
            val selectedOrder = preferences[PreferencesKey.selectedOrder] ?: DEFAULT_ORDER
            val selectedOrderId = preferences[PreferencesKey.selectedOrderId] ?: 0
            TradingMarketFilter(
                selectedCurrency,
                selectedCurrencyId,
                selectedOrder,
                selectedOrderId
            )
        }
}

data class TradingMarketFilter(
    val selectedCurrency: String,
    val selectedCurrencyId: Int,
    val selectedOrder: String,
    val selectedOrderId: Int
)