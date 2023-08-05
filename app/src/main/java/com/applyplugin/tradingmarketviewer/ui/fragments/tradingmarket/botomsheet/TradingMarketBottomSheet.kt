package com.applyplugin.tradingmarketviewer.ui.fragments.tradingmarket.botomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.applyplugin.tradingmarketviewer.R
import com.applyplugin.tradingmarketviewer.databinding.TradingmarketBottomSheetBinding
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.CURRENCY
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.ORDER
import com.applyplugin.tradingmarketviewer.viewmodels.TradingMarketViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale

class TradingMarketBottomSheet : BottomSheetDialogFragment() {

    private lateinit var tradingMarketViewModel: TradingMarketViewModel

    private var _binding: TradingmarketBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var currency = CURRENCY
    private var currency_id = 0
    private var order = ORDER
    private var order_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tradingMarketViewModel = ViewModelProvider(requireActivity()).get(TradingMarketViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  TradingmarketBottomSheetBinding.inflate(inflater, container, false)

        tradingMarketViewModel.readTradingMarketFilter.asLiveData().observe(viewLifecycleOwner){value ->
            currency = value.selectedCurrency
            order = value.selectedOrder
            updateChips(value.selectedCurrencyId, binding.currencyChipGroup)
            updateChips(value.selectedOrderId, binding.orderChipGroup)
        }

        @Suppress("DEPRECATION")
        binding.currencyChipGroup.setOnCheckedChangeListener(){group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedCurrency = chip.text.toString().lowercase(Locale.ROOT)
            currency = selectedCurrency
            currency_id = selectedChipId
        }

        @Suppress("DEPRECATION")
        binding.orderChipGroup.setOnCheckedChangeListener(){group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedOrder = chip.text.toString().lowercase(Locale.ROOT)
            order = selectedOrder
            order_id = selectedChipId
        }

        binding.applyButton.setOnClickListener {
            tradingMarketViewModel.saveTradingMarketFilter(
                currency,
                currency_id,
                order,
                order_id
            )

            val action =
                TradingMarketBottomSheetDirections.actionTradingMarketBottomSheetToTradingMarketFragment(true)
            findNavController().navigate(action)
        }

        return binding.root

    }

    private fun updateChips(chipId: Int, currencyChipGroup: ChipGroup) {
        if(chipId != 0){
            try{
                currencyChipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e: Exception){
                Log.d("TradingMarketBottomSheet", e.message.toString())
            }
        }
    }
}