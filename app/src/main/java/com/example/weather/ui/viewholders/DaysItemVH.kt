package com.example.weather.ui.viewholders

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.RequestManager
import com.example.weather.R
import com.example.weather.data.responses.DayResponse

class DaysItemVH(val day: DayResponse?,  val date: String?, val requestManager: RequestManager) : AbstractViewHolder() {

    override fun getLayoutIdentifier(): Int {
        return R.layout.layout_days_item
    }

    companion object {

        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(v: ImageView, model: DaysItemVH) {
            model.requestManager.load(model.day?.dayCondition?.icon?.replace("//", "https://")).into(v)
        }
    }
}