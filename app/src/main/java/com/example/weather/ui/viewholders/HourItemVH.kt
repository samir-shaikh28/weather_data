package com.example.weather.ui.viewholders

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.data.responses.HourResponse

class HourItemVH( val hourResponse: HourResponse, val requestManager: RequestManager) : AbstractViewHolder() {

    override fun getLayoutIdentifier(): Int {
        return R.layout.layout_hour_item
    }

    companion object {

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(v: ImageView, model: HourItemVH) {
            model.requestManager.load(model.hourResponse.condition?.icon?.replace("//", "https://")).into(v)
        }
    }
}