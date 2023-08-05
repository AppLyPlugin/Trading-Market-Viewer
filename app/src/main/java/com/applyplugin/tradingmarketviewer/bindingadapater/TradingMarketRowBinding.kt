package com.applyplugin.tradingmarketviewer.bindingadapater

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Precision
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.applyplugin.tradingmarketviewer.R

class TradingMarketRowBinding {

    companion object {

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {

            imageView.load(
                imageUrl
            ) {
                crossfade(true)
                placeholder(R.drawable.ic_error_loading_image)
                error(R.drawable.ic_error_loading_image)
                transformations(CircleCropTransformation())
                precision(Precision.EXACT)
                scale(Scale.FILL)
            }
        }

        @BindingAdapter("setCurrentPrice")
        @JvmStatic
        fun setCurrentPrice(textView: TextView, currentPrice: Double) {
            textView.text = "$$currentPrice"

        }

        @BindingAdapter("setPriceChange24h")
        @JvmStatic
        fun setPriceChange24h(textView: TextView, priceChange: Double) {
            textView.text = "${String.format("%.4f", Math.abs(priceChange))}"
        }

        @BindingAdapter("setPriceChangePer24h")
        @JvmStatic
        fun setPriceChangePer24h(textView: TextView, priceChangePer: Double) {
            textView.text = "${String.format("%.2f", priceChangePer)}%"
        }

        @BindingAdapter("setValueFontColor")
        @JvmStatic
        fun setValueFontColor(textView: TextView, percentValue: Double) {
            var textColor = when {
                percentValue < 0 -> R.color.red
                percentValue > 0 -> R.color.green
                else -> R.color.mediumGray
            }

            textView.setTextColor(ContextCompat.getColor(textView.context, textColor))
        }
    }
}