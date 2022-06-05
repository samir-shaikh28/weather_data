package com.example.weather.ui.viewholders

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.data.responses.CurrentResponse
import com.example.weather.data.responses.CurrentWeather
import com.example.weather.data.responses.LocationResponse
import com.example.weather.listener.OnBookMarkClickListener

class CurrentWeatherVH(
    val current: CurrentResponse?,
    val location: LocationResponse?,
    var isBookMark: Boolean,
    val requestManager: RequestManager,
    val listener: OnBookMarkClickListener
) :
    AbstractViewHolder() {

    var isBookMarkObserver: ObservableBoolean = ObservableBoolean(isBookMark)

    override fun getLayoutIdentifier(): Int {
        return R.layout.layout_current_weather
    }

    fun onBookMarkClick(v: View) {
        (v as? AppCompatCheckBox)?.apply {
            isBookMarkObserver.set(isChecked)
        }
        if (isBookMarkObserver.get()) {
            listener.addToFavourites(location)
        } else {
            listener.removeFromFavourite("${location?.lat},${location?.lon}")
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(v: ImageView, model: CurrentWeatherVH) {
            model.requestManager.load(model.current?.condition?.icon?.replace("//", "https://")).into(v)
        }

        @JvmStatic
        @BindingAdapter("setVectorSelector")
        fun mediaItemImage(checkbox: AppCompatCheckBox, model: CurrentWeatherVH) {
            checkbox.setButtonDrawable(R.drawable.bg_bookmark_inset)
        }
    }


}