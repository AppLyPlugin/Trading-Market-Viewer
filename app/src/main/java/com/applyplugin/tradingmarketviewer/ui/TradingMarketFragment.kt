package com.applyplugin.tradingmarketviewer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.applyplugin.tradingmarketviewer.MainViewModel
import com.applyplugin.tradingmarketviewer.adapter.TradingMarketAdapter
import com.applyplugin.tradingmarketviewer.databinding.FragmentTradingmarketBinding
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.API_DELAY
import com.applyplugin.tradingmarketviewer.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TradingMarketFragment : Fragment() {

    private val mAdapter: TradingMarketAdapter by lazy { TradingMarketAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private val tradingMarketViewModel: TradingMarketViewModel by viewModels()

    private var _binding: FragmentTradingmarketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTradingmarketBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setUpRecyclerView()

        requestApiData()

        return binding.root
    }

    private fun requestApiData() {
        Log.d("TradingMarketFragment", "requestApiData called!")
        mainViewModel.getTradingMarketData(tradingMarketViewModel.applyQuery())
        mainViewModel.tradingMarketResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        Log.d("TradingMarketFragment", "Load From Cache")
        lifecycleScope.launch {
            mainViewModel.readTradingMarket.observe(viewLifecycleOwner) { database ->
                try {
                    mAdapter.setData(database[0].tradingMarket)
                } catch (e: Exception) {
                    Log.d("TradingMarketFragment", "Cache Empty")
                    binding.errorImg.visibility = View.VISIBLE;
                    binding.errorTxt.visibility = View.VISIBLE;
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.tradingmarketShimmer.startShimmer()
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.tradingmarketShimmer.stopShimmer()
        binding.tradingmarketShimmer.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}