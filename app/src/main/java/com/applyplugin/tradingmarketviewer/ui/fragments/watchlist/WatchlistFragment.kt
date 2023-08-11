package com.applyplugin.tradingmarketviewer.ui.fragments.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.tradingmarketviewer.R
import com.applyplugin.tradingmarketviewer.adapter.WatchlistAdapter
import com.applyplugin.tradingmarketviewer.databinding.WatchlistFragmentBinding
import com.applyplugin.tradingmarketviewer.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : Fragment() {

    private val mAdapter: WatchlistAdapter by lazy { WatchlistAdapter(requireActivity(), mainViewModel) }
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: WatchlistFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = WatchlistFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpRecyclerView(binding.recyclerview)

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true);

        mainViewModel.readWatchlist.observe(viewLifecycleOwner) { watchlistEntity ->
            mAdapter.setData(watchlistEntity)
        }

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_all_watchlist, menu)
    }

    private fun setUpRecyclerView(recyclerview: RecyclerView) {
        recyclerview.adapter = mAdapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            @Suppress("DEPRECATION")
            fragmentManager?.popBackStackImmediate()
        }else if(item.itemId == R.id.delete_all_watchlist){
            mainViewModel.deleteAllWatchlist()
            showDeleteAllSnackBar()
        }

        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteAllSnackBar() {
        Snackbar.make(
            binding.root,
            "All Watchlist Deleted",
            Snackbar.LENGTH_SHORT
        ).setAction("OK"){}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.clearContextualActionMode()
        _binding = null
    }
}