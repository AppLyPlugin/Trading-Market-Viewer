package com.applyplugin.tradingmarketviewer.ui.fragments.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.applyplugin.tradingmarketviewer.R
import com.applyplugin.tradingmarketviewer.databinding.FragmentDetailsBinding
import com.applyplugin.tradingmarketviewer.model.TradingMarketResponse
import com.applyplugin.tradingmarketviewer.util.Constants.Companion.TRADINGMARKET_BUNDLE

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val args = arguments
        var bundle: TradingMarketResponse?

        if (Build.VERSION.SDK_INT >= 33) {
            bundle = args?.getParcelable(TRADINGMARKET_BUNDLE, TradingMarketResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle = args?.getParcelable(TRADINGMARKET_BUNDLE)
        }

        binding.ivLogo.load(bundle?.image)
        binding.tvRank.text = "Rank: ${bundle?.marketCapRank.toString()}"
        binding.tvCurrentPrice.text = bundle?.currentPrice.toString()
        binding.tvPriceChange24h.text = bundle?.priceChange24h.toString()
        setValueFontColor(binding.tvPriceChange24h, bundle?.priceChange24h!!)
        binding.tvPriceChange24hPer.text = "${bundle?.priceChangePercentage24h.toString()}%"
        setValueFontColor(binding.tvPriceChange24hPer, bundle?.priceChangePercentage24h!!)
        binding.tvLblLastUpdated.text =
            "Last Updated: ${setDate(bundle?.lastUpdated!!)} ${setTime(bundle?.lastUpdated!!)}"
        binding.tvName.text = bundle?.name
        binding.tvSymbol.text = bundle?.symbol?.uppercase()
        binding.tvMarketCap.text =
            "${bundle?.marketCap?.toBigDecimal()?.toPlainString()}"
        binding.tvTotalVolume.text =
            "${bundle?.totalVolume?.toBigDecimal()?.toPlainString()}"
        binding.tvCirculatingSupply.text =
            "${bundle?.circulatingSupply?.toBigDecimal()?.toPlainString()}"
        binding.tv24hHigh.text = bundle?.high24h.toString()
        binding.tv24hLow.text = bundle?.low24h.toString()
        binding.tvMarketCapChange.text = bundle?.marketCapChange24h.toString()
        binding.tvMarketCapChangePer.text = bundle?.marketCapChangePercentage24h.toString()
        binding.tvAth.text = bundle?.ath.toString()
        binding.tvAthDate.text = "${setDate(bundle?.athDate!!)} ${setTime(bundle?.athDate!!)}"
        binding.tvAthChange.text = bundle?.athChangePercentage.toString()
        binding.tvAtl.text = bundle?.atl.toString()
        binding.tvAtlDate.text = "${setDate(bundle?.atlDate!!)} ${setTime(bundle?.atlDate!!)}"
        binding.tvAtlChange.text = bundle?.atlChangePercentage.toString()

        return binding.root
    }

    fun setTime(last_updated: String): String {
        val dateAndTime = last_updated.split("T").toTypedArray()
        val time = dateAndTime[1].split(".").toTypedArray()
        return time[0]

    }

    fun setDate(last_updated: String): String {
        val dateAndTime = last_updated.split("T").toTypedArray()
        return dateAndTime[0].substring(2, dateAndTime[0].length)
    }

    fun setValueFontColor(textView: TextView, percentValue: Double) {
        var textColor = when {
            percentValue < 0 -> R.color.red
            percentValue > 0 -> R.color.green
            else -> R.color.mediumGray
        }

        textView.setTextColor(ContextCompat.getColor(textView.context, textColor))
    }
}