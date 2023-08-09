package com.applyplugin.tradingmarketviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import com.applyplugin.tradingmarketviewer.databinding.ActivityDetailsBinding
import com.applyplugin.tradingmarketviewer.ui.fragments.details.DetailsFragment
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.TRADINGMARKET_BUNDLE

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}