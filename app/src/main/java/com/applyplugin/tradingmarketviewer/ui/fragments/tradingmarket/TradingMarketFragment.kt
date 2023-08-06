package com.applyplugin.tradingmarketviewer.ui.fragments.tradingmarket

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.applyplugin.tradingmarketviewer.R
import com.applyplugin.tradingmarketviewer.viewmodels.MainViewModel
import com.applyplugin.tradingmarketviewer.adapter.TradingMarketAdapter
import com.applyplugin.tradingmarketviewer.databinding.TradingmarketFragmentBinding
import com.applyplugin.tradingmarketviewer.util.NetworkListener
import com.applyplugin.tradingmarketviewer.viewmodels.TradingMarketViewModel
import com.applyplugin.tradingmarketviewer.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TradingMarketFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mAdapter: TradingMarketAdapter by lazy { TradingMarketAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private val tradingMarketViewModel: TradingMarketViewModel by viewModels()

    private var _binding: TradingmarketFragmentBinding? = null
    private val binding get() = _binding!!

    private val arg by navArgs<TradingMarketFragmentArgs>()

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = TradingmarketFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        setUpRecyclerView()

        tradingMarketViewModel.readBackOnline.observe(viewLifecycleOwner){
            tradingMarketViewModel.backOnline = it
        }

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect{status ->
                    tradingMarketViewModel.networkStatus = status
                    tradingMarketViewModel.showNetworkStatus()
                    if(status){
                        requestApiData()
                    }else{
                        loadDataFromCache()
                    }
                }

        }

        binding.filterFab.setOnClickListener {
            if(tradingMarketViewModel.networkStatus) {
                findNavController().navigate(R.id.action_tradingMarketFragment_to_tradingMarketBottomSheet)
            }else{
                tradingMarketViewModel.showNetworkStatus()
            }
        }

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

    private fun searchApiData(searchIds: String){
        showShimmerEffect()
        mainViewModel.searchData(tradingMarketViewModel.applySearchQuery(searchIds))
        mainViewModel.searchedTradingMarketResponse.observe(viewLifecycleOwner){response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val tradingMarketData = response.data
                    tradingMarketData?.let {
                        mAdapter.setData(it)
                    }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tradingmarket_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.queryHint = getString(R.string.enter_crypto_name)
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}